package com.zepp.golfsense.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;

import com.zepp.baseball.R;
import com.zepp.golfsense.utils.ImageUtil;
import com.zepp.golfsense.utils.LogUtil;

@SuppressLint("NewApi")
public class ReviewDrawView extends TextureView implements TextureView.SurfaceTextureListener {

	public String TAG = this.getClass().getSimpleName();
	private float mX, mY;
	private float defaultOvalCenterX;
	private float defaultOvalCenterY;
	private float defaultLineStartX;
	private float defaultLineStartY;
	private float defaultLineEndX;
	private float defaultLineEndY;
	private float defaultRadius;
	private ArrayList<Shape> shapeList;
	public int shapeCount = 0;

	float EPSILON = 0.001f;
	private int width;
	private int height;
	private Bitmap editBitmap;
	private float clickX;
	private float clickY;
	public boolean isValidate = false;
	private Shape currentShape;
	private int position;
	private Paint mPaint;

	private Canvas mCanvas;
	private boolean isFocus = true;
	private int[] locations;
	private int top;
	private int left;

	private boolean surfaceAvailable = false;

	public ReviewDrawView(Context context) {
		super(context);
		locations = new int[2];
		getLocationOnScreen(locations);
		top = locations[0];
		left = locations[1];
		setOpaque(false);
	}

	public ReviewDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setSurfaceTextureListener(this); // 添加状态监听

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.YELLOW);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(8);
		setOpaque(false);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		if (!isInEditMode()) {
			LogUtil.i(TAG, "onSizeChange w,h=" + w + "," + h);
		}

		/**
		 * init coordinate
		 */
		defaultOvalCenterX = width / 2;
		defaultOvalCenterY = height / 2;
		defaultRadius = 60;
		defaultLineStartX = defaultOvalCenterX - 80;
		defaultLineStartY = defaultOvalCenterY - 80;
		defaultLineEndX = defaultOvalCenterX + 80;
		defaultLineEndY = defaultOvalCenterY + 80;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isFocus) {
			return false;
		}
		float x = event.getX();
		float y = event.getY();

		Point point = new Point();
		point.x = (int) x;
		point.y = (int) y;
		// Shape shape = getTopShape(point);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// touch_start(x, y);
			mX = x;
			mY = y;
			clickX = x;
			clickY = y;
			if (currentShape != null)
				position = currentShape.whichBitmap(point);
			break;
		case MotionEvent.ACTION_MOVE:
			// touch_move(x, y);
			float dx = x - mX;
			float dy = y - mY;
			System.out.println("############### " + dx + "############" + dy);
			if (currentShape != null) {
				int type = currentShape.getType();
				if (type == 0) {
					Line line = (Line) currentShape;
					if (!line.isEdit) {
						LogUtil.d(TAG, "line not translate#####################");
						return true;
					}
					switch (position) {
					case 0:// center
						currentShape.translate(dx, dy);
						break;
					case 1:// left
						line.lineSide = 0;
						currentShape.scale(dx, dy);
						break;
					case 2:// right;
						line.lineSide = 1;
						currentShape.scale(dx, dy);
						break;
					default:
						line.lineSide = -1;
						break;
					}
				} else if (type == 2) {
					Circle circle = (Circle) currentShape;
					if (!circle.isEdit) {
						LogUtil.d(TAG, "circle not translate#####################");
						return true;
					}
					// int position = currentShape.whichBitmap(point);
					if (position == 0) {
						// center
						currentShape.translate(dx, dy);
					} else if (position == 1) {
						currentShape.scale(dx, dy);
					}
				}
				// invalidate();
				Draw();
			}
			mX = x;
			mY = y;
			break;
		case MotionEvent.ACTION_UP:
			if ((clickX >= x - 5 && clickX <= x + 5) && (clickY >= y - 5 && clickY <= y + 5)) {
				click(x, y);
			}
			// click
			break;

		}
		return true;
	}

	/**
	 * click
	 * 
	 * @param x
	 * @param y
	 */
	private void click(float x, float y) {
		Point point = new Point();
		point.x = (int) x;
		point.y = (int) y;
		currentShape = getTopShape(point);
		if (currentShape != null) {
			if (currentShape.getType() == 0) {
				Line line = (Line) currentShape;
				line.isEdit = !line.isEdit;
				if (line.isEdit) {
					resetShape();
					line.isEdit = true;
				}
				currentShape = line;
				LogUtil.d(TAG, "############# line click trigger ###########");
			} else if (currentShape.getType() == 2) {
				Circle circle = (Circle) currentShape;
				circle.isEdit = !circle.isEdit;
				if (circle.isEdit) {
					resetShape();
					circle.isEdit = true;
				}
				currentShape = circle;
				LogUtil.d(TAG, "############# circle click trigger ###########");
			}
			editBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.review_video_bezier);
			editBitmap = compress(editBitmap, 30, 30);
		} else {
			resetShape();
		}
		// invalidate();
		Draw();
	}

	public void resetShape() {
		if (shapeList != null) {
			for (int i = 0; i < shapeList.size(); i++) {
				shapeList.get(i).isEdit = false;
			}
		}
	}

	/**
	 * get the topside of overlap shapes
	 * 
	 * @return
	 */
	public Shape getTopShape(Point point) {
		int x = point.x;
		int y = point.y;
		if (shapeList != null) {
			for (int i = shapeList.size() - 1; i >= 0; i--) {
				Shape shape = shapeList.get(i);
				if (shape.getType() == 0) {
					// line
					Line line = (Line) shape;
					float startX = line.startX < line.endX ? line.startX : line.endX;
					float endX = line.startX < line.endX ? line.endX : line.startX;
					float startY = line.startY < line.endY ? line.startY : line.endY;
					float endY = line.startY < line.endY ? line.endY : line.startY;
					// try {
					// RectF rectf = new RectF(startX, startY, endX, endY);
					// float radio1 = (endY - startY) / (endX - startX);
					// float radio2 = (startY - (float) point.y) / (startX -
					// (float) point.x);
					// if (rectf.contains(point.x, point.y) && radio2 > radio1 -
					// 0.2 && radio2 < radio1 + 0.2) {
					// return line;
					// }
					// } catch (RuntimeException e) {
					// return line;
					// }

					// D1
					double d1 = Math.sqrt(Math.pow((point.x - startX), 2) + Math.pow((point.y - startY), 2));
					double d2 = Math.sqrt(Math.pow((point.x - endX), 2) + Math.pow((point.y - endY), 2));
					double d = Math.sqrt(Math.pow((endX - startX), 2) + Math.pow((endY - startY), 2));
					if (d1 + d2 < d + 2) {
						return line;
					}

					// RectF rectf = new RectF(startX, startY, endX, endY);
					// if (rectf.contains(point.x, point.y)) {
					// return line;
					// }
					// if (x > startX && x < endX && y > startY && y < endY) {
					// return line;
					// }
				} else if (shape.getType() == 2) {
					Circle circle = (Circle) shape;
					float x2 = (float) Math.pow(Math.abs(circle.centerX - point.x), 2);
					float y2 = (float) Math.pow(Math.abs(circle.centerY - point.y), 2);
					float dm = (float) Math.sqrt(x2 + y2);
					if (dm > circle.radius - 20 && dm < circle.radius + 20) {
						return circle;
					}
				}
			}
		}
		return null;
	}

	public Shape getScacleShape(Point point) {
		int x = point.x;
		int y = point.y;
		for (int i = shapeList.size() - 1; i >= 0; i--) {
			Shape shape = shapeList.get(i);
			if (shape.getType() == 0) {
				// line
				Line line = (Line) shape;
				if (x == Math.abs(line.startX - line.endX) / 2 && y == Math.abs(line.startY - line.endY) / 2) {
					return line;
				}
			} else if (shape.getType() == 1) {
				Circle circle = (Circle) shape;
				if ((x == Math.abs(circle.centerX + defaultRadius) || x == Math.abs(circle.centerX - defaultRadius))
						&& (y == Math.abs(circle.centerY + defaultRadius) || y == Math.abs(circle.centerY
								- defaultRadius))) {
					return circle;
				}
			}
		}
		return null;
	}

	public void clear() {
		Canvas canvas = new Canvas();
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		canvas.drawPaint(paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC));

		// invalidate();
		Draw();
	}

	/**
	 * draw line
	 */
	public void drawLine() {
		if (shapeList == null) {
			shapeList = new ArrayList<Shape>();
		}else{
			if(shapeList.size()>=10){
				return;
			}
		}
		shapeCount++;
		Line line = new Line();
		line.shapeColor = mPaint.getColor();
		line.priority = shapeCount;
		line.startX = defaultLineStartX;
		line.startY = defaultLineStartY;
		line.endX = defaultLineEndX;
		line.endY = defaultLineEndY;
        line.isEdit = true;

        for(int i = 0;i<shapeList.size();i++){
            Shape shape = shapeList.get(i);
            shape.isEdit = false;
        }
		shapeList.add(line);
        currentShape = line;
		Draw();

	}

	/**
	 * draw oval
	 */
	public void drawOval() {
		if (shapeList == null) {
			shapeList = new ArrayList<Shape>();
		}else{
			if(shapeList.size()>=10){
				return;
			}
		}
		shapeCount++;
		Circle circle = new Circle();
		circle.shapeColor = mPaint.getColor();
		circle.priority = shapeCount;
		circle.centerX = defaultOvalCenterX;
		circle.centerY = defaultOvalCenterY;
		circle.endY = circle.centerY + defaultRadius;
		circle.radius = defaultRadius;
        circle.isEdit = true;

        for(int i = 0;i<shapeList.size();i++){
            Shape shape = shapeList.get(i);
            shape.isEdit = false;
        }
		shapeList.add(circle);
        currentShape = circle;
		Draw();
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		surfaceAvailable = true;
		Draw();
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
		Draw();
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		surfaceAvailable = false;
		return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {

	}

	private Paint initBlackPaint() {
		Paint mPaint1 = new Paint();
		mPaint1.setAntiAlias(true);
		mPaint1.setDither(true);
		mPaint1.setColor(Color.BLACK);
		mPaint1.setStyle(Paint.Style.STROKE);
		mPaint1.setStrokeJoin(Paint.Join.ROUND);
		mPaint1.setStrokeCap(Paint.Cap.ROUND);
		mPaint1.setStrokeWidth(12);
		return mPaint1;
	}

	public abstract class Shape {
		public List<Rect> bitmapRect;
		public boolean isEdit = false;

		public int type;
		public int shapeColor;

		public int isHidden = 0;

		public abstract int getType();

		public abstract void draw(Canvas canvas);

		public abstract void translate(float dx, float dy);

		public abstract void scale(float dx, float dy);

		public abstract int whichBitmap(Point point);
	}

	public class Circle extends Shape {
		public int shapeColor;
		public int type = 2;
		public int priority = -1;
		public float centerX;
		public float centerY;
		public float endX;
		public float endY;
		public float radius;

		public Circle() {
			Rect rst1 = new Rect();// center
			Rect rst2 = new Rect();// top
			bitmapRect = new ArrayList<Rect>();
			bitmapRect.add(rst1);
			bitmapRect.add(rst2);
		}

		@Override
		public int getType() {
			return type;
		}

		@Override
		public void draw(Canvas canvas) {
			if (this.isEdit) {

				// Log.d(TAG, "current circle radius ===" + this.radius);
				LogUtil.e(TAG, "circle radius ===" + this.radius);
				Paint blackPaint = initBlackPaint();
				canvas.drawCircle(this.centerX, this.centerY, this.radius, blackPaint);
				canvas.drawCircle(this.centerX, this.centerY, this.radius, mPaint);

				// canvas.drawCircle(this.centerX, this.centerY, this.radius
				// + mPaint.getStrokeWidth() / 2 + 1, blackPaint);
				// canvas.drawCircle(this.centerX, this.centerY, this.radius
				// - mPaint.getStrokeWidth() / 2 - 1, blackPaint);
				drawBitmapOnCircle(canvas, this);
			} else {
				canvas.drawCircle(this.centerX, this.centerY, this.radius, mPaint);
			}
		}

		@Override
		public void translate(float dx, float dy) {
			centerX = centerX + dx;
			centerY = centerY + dy;
			endY = centerY - radius;
		}

		@Override
		public void scale(float dx, float dy) {
			if (dy < 0) {
				if (this.radius < height / 3) {
					this.radius = this.radius + Math.abs(dy);
				}
			} else if (dy > 0) {
				if (this.radius > 40) {
					this.radius = this.radius - Math.abs(dy);
				}
			}
			this.endY = this.centerY - radius;

			// radius = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

			LogUtil.d(TAG, "circle radius ############ = " + radius);
		}

		@Override
		public int whichBitmap(Point point) {
			for (int i = 0; i < bitmapRect.size(); i++) {
				if (bitmapRect.get(i).contains(point.x, point.y)) {
					return i;
				}
			}
			return -1;
		}
	}

	public class Line extends Shape {
		public int priority = -1;
		public int type = 0;
		public int shapeColor;
		public float startX;
		public float startY;
		public float endX;
		public float endY;
		public int lineSide = -1; // 0 left 1 right

		public Line() {
			Rect rst1 = new Rect();// center
			Rect rst2 = new Rect();// left
			Rect rst3 = new Rect();// right
			bitmapRect = new ArrayList<Rect>();
			bitmapRect.add(rst1);
			bitmapRect.add(rst2);
			bitmapRect.add(rst3);
		}

		@Override
		public int getType() {
			return type;
		}

		@Override
		public void draw(Canvas canvas) {
			if (isEdit) {
				canvas.drawLine(this.startX, this.startY, this.endX, this.endY, mPaint);

				// float startX = this.startX < this.endX ? this.startX
				// : this.endX;
				// float endX = this.startX < this.endX ? this.endX :
				// this.startX;
				// float startY = this.startY < this.endY ? this.startY
				// : this.endY;
				// float endY = this.startY < this.endY ? this.endY : startY;
				Paint paint = initBlackPaint();
				canvas.drawLine(this.startX, this.startY, this.endX, this.endY, paint);
				canvas.drawLine(this.startX, this.startY, this.endX, this.endY, mPaint);
				// if (this.endX == this.startX) {
				// // vertical
				// // left
				// canvas.drawLine(this.startX - mPaint.getStrokeWidth() / 2
				// - 1, this.startY,
				// this.endX - mPaint.getStrokeWidth() / 2 - 1,
				// this.endY, paint);
				//
				// // right
				// canvas.drawLine(this.startX + mPaint.getStrokeWidth() / 2
				// + 1, this.startY,
				// this.endX + mPaint.getStrokeWidth() / 2 + 1,
				// this.endY, paint);
				// } else if (this.startY == this.endY) {
				// // horizontal
				//
				// // up
				// canvas.drawLine(this.startX,
				// this.startY - mPaint.getStrokeWidth() / 2 - 1,
				// this.endX, this.endY - mPaint.getStrokeWidth() / 2
				// - 1, paint);
				//
				// // down
				// canvas.drawLine(this.startX,
				// this.startY + mPaint.getStrokeWidth() / 2 + 1,
				// this.endX, this.endY + mPaint.getStrokeWidth() / 2
				// + 1, paint);
				// } else {
				// // radio = (this.endY - this.startY)
				// // / (this.endX - this.startX);
				//
				// LogUtil.d(TAG, "startX =" + this.startX + ": startY = " +
				// this.startY);
				// canvas.drawLine(this.startX,
				// this.startY - mPaint.getStrokeWidth() / 2 - 1,
				// this.endX, this.endY - mPaint.getStrokeWidth() / 2
				// - 1, paint);
				// canvas.drawLine(this.startX,
				// this.startY + mPaint.getStrokeWidth() / 2 + 1,
				// this.endX, this.endY + mPaint.getStrokeWidth() / 2
				// + 1, paint);
				// }

				drawBitmapOnLine(canvas, this);
			} else {
				canvas.drawLine(this.startX, this.startY, this.endX, this.endY, mPaint);
			}
		}

		@Override
		public void translate(float dx, float dy) {
			startX = startX + dx;
			endX = endX + dx;
			startY = startY + dy;
			endY = endY + dy;
		}

		@Override
		public void scale(float dx, float dy) {
			if (lineSide == 0) {
				// left
				startX = startX + dx;
				startY = startY + dy;
			} else if (lineSide == 1) {
				// right
				endX = endX + dx;
				endY = endY + dy;
			}
		}

		@Override
		public int whichBitmap(Point point) {
			for (int i = 0; i < bitmapRect.size(); i++) {
				if (bitmapRect.get(i).contains(point.x, point.y)) {
					// i=0 center i=1 left i=2 right
					return i;
				}
			}

			if (getTopShape(point) != null) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	public boolean isPointOnLine(Point linePointA, Point linePointB, Point point) {
		float m = (linePointB.y - linePointA.y) / (linePointB.x - linePointA.x);
		float b = linePointA.y - m * linePointA.x;
		if (Math.abs(point.y - (m * point.x + b)) < EPSILON) {

			return true;
		}

		return false;
	}

	private Bitmap compress(Bitmap image, int width, int height) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = height;// 这里设置高度为800f
		float ww = width;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public void drawBitmapOnLine(Canvas canvas, Line line) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.review_video_bezier);
		bitmap = ImageUtil.tochange(bitmap, bitmap.getWidth() / 2, bitmap.getWidth() / 2);
		// bitmap = compress(bitmap, 30, 30);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		LogUtil.d(TAG, "drawBitmapOnLine edit bitmap width ==========" + width);
		LogUtil.d(TAG, "drawBitmapOnLine edit bitmap width ==========" + height);
		float lineCenterX = (line.startX + line.endX) / 2;
		float lineCenterY = (line.startY + line.endY) / 2;
		// Rect dst1 = line.bitmapRect.get(0);
		// // draw centerbitmap
		// float left = lineCenterX - width / 2;
		// float top = lineCenterY - height / 2;
		// float right = lineCenterX + width / 2;
		// float bottom = lineCenterY + height / 2;
		// dst1.left = (int) left;
		// dst1.top = (int) top;
		// dst1.right = (int) right;
		// dst1.bottom = (int) bottom;
		// canvas.drawBitmap(bitmap, null, dst1, null);
		// canvas.drawBitmap(bitmap, left, top, null);

		Rect dst2 = line.bitmapRect.get(1);
		// draw leftSidebitmap
		float left = line.startX - width / 2;
		float top = line.startY - height / 2;
		float right = line.startX + width / 2;
		float bottom = line.startY + height / 2;
		dst2.left = (int) left;
		dst2.top = (int) top;
		dst2.right = (int) right;
		dst2.bottom = (int) bottom;
		// canvas.drawBitmap(bitmap, left, top, null);
		canvas.drawBitmap(bitmap, null, dst2, null);

		Rect dst3 = line.bitmapRect.get(2);
		// draw rightSidebitmap
		left = line.endX - width / 2;
		top = line.endY - height / 2;
		right = line.endX + width / 2;
		bottom = line.endY + width / 2;
		dst3.left = (int) left;
		dst3.top = (int) top;
		dst3.right = (int) right;
		dst3.bottom = (int) bottom;
		// canvas.drawBitmap(bitmap, left, top, null);
		canvas.drawBitmap(bitmap, null, dst3, null);
	}

	public void drawBitmapOnCircle(Canvas canvas, Circle circle) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.review_video_bezier);
		bitmap = ImageUtil.tochange(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		// bitmap = compress(bitmap, 30, 30);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		LogUtil.d(TAG, "drawBitmapOnCircle edit bitmap width ==========" + width);
		LogUtil.d(TAG, "drawBitmapOnCircle edit bitmap width ==========" + height);

		float left = circle.centerX - width / 2;
		float top = circle.centerY - height / 2;
		float right = circle.centerX + width / 2;
		float bottom = circle.centerY + height / 2;

		// draw centerbitmap
		Rect dst1 = circle.bitmapRect.get(0);
		dst1.left = (int) left;
		dst1.top = (int) top;
		dst1.right = (int) right;
		dst1.bottom = (int) bottom;
		canvas.drawBitmap(bitmap, null, dst1, null);

		// draw Topbitmap
		// top = circle.centerY + circle.radius - height;
		left = circle.centerX - width / 2;
		top = circle.centerY - circle.radius - height / 2;
		right = circle.centerX + width / 2;
		bottom = circle.centerY - circle.radius + height / 2;
		Rect dst2 = circle.bitmapRect.get(1);
		dst2.left = (int) left;
		dst2.top = (int) top;
		dst2.right = (int) right;
		dst2.bottom = (int) bottom;
		canvas.drawBitmap(bitmap, null, dst2, null);
	}

	private void Draw(){
		if (!surfaceAvailable) {
			return;
		}

		mCanvas = lockCanvas();
		if(mCanvas == null){
			throw new RuntimeException("can not be edited");
		}
		mCanvas.drawColor(0, Mode.CLEAR);
		if (shapeList != null) {
			for (int i = 0; i < shapeList.size(); i++) {
				Shape shape = shapeList.get(i);
				shape.draw(mCanvas);
			}
		}

		unlockCanvasAndPost(mCanvas);
	}

	/**
	 * delete shape from currrent view
	 */
	public void deleteShape() {
		if (shapeList != null) {
			for (int i = 0; i < shapeList.size(); i++) {
				Shape shape = shapeList.get(i);
				if (shape.isEdit) {
					shapeList.remove(i);
				}
			}
		}
		Draw();
	}

	public void clearListData() {
		if (shapeList != null) {
			shapeList.clear();
		}
	}

	/**
	 * parse shape datas to String
	 * 
	 * @param
	 * @return
	 */
	public String parseShapeList2String(int width, int height,int viewWidth, int viewHeight) {
		if (width == 0 || height == 0) {
			return null;
		}

		int finalWidth = viewWidth;
		int finalHeight = finalWidth  * height / width;
		if (finalHeight > viewHeight) {
			finalHeight = viewHeight;
			finalWidth = finalHeight * width / height;
		}

		int leftDiff = (viewWidth - finalWidth) / 2;
		int topDiff = (viewHeight - finalHeight) / 2;

		LogUtil.d(TAG, "parseShapeList2String"
				+ " video size " + width + "x" + height
				+ " view size " + viewWidth + "x" + viewHeight
				+ " final size " + finalWidth + "x" + finalHeight
				+ " offset " + leftDiff + "x" + topDiff);

		StringBuilder totalSb = new StringBuilder();
		if (shapeList != null && shapeList.size() > 0) {

			for (int i = 0; i < shapeList.size(); i++) {
				StringBuilder sb = new StringBuilder();
				Shape shape = shapeList.get(i);
//				sb.append("<");
				if (shape.getType() == 0) {
					// line
					Line line = (Line) shape;

					sb.append(line.type + "|");
					sb.append(line.shapeColor + "|");
					sb.append(line.isHidden + "|");
					sb.append(((line.startX - leftDiff) / (float)finalWidth * width ) +  "|");
					sb.append(((line.startY - topDiff) / (float)finalHeight * height) + "|");
					sb.append(((line.endX - leftDiff) / (float)finalWidth * width) +  "|");
					sb.append(((line.endY - topDiff) / (float)finalHeight * height) + "");

				} else if (shape.getType() == 2) {
					// circle
					Circle circle = (Circle) shape;
					sb.append(circle.type + "|");
					sb.append(circle.shapeColor + "|");
					sb.append(circle.isHidden + "|");
					sb.append(((circle.centerX - leftDiff) / (float)finalWidth * width) +  "|");
					sb.append(((circle.centerY - topDiff) / (float)finalHeight * height) + "|");
					sb.append((circle.endX - leftDiff) / (float)finalWidth * width + "|");
					sb.append((circle.endY - topDiff) / (float)finalHeight * height + "");
				}
//				sb.append(">" + ",");
				sb.append(",");
				totalSb.append(sb);
			}
			LogUtil.i(TAG, "shape String data" + totalSb.substring(0, totalSb.lastIndexOf(",")));
			return totalSb.substring(0, totalSb.lastIndexOf(","));
		}
		return null;

	}

	/**
	 * parse String to shapelist
	 * 
	 * <2|-65536|0|778.0|377.0|778.0|177.0>,
	 * <0|-65536|0|253.0|195.0|471.0|650.0>,
	 * <2|-65536|0|786.7509|950.751|786.7509|825.751>,
	 * <2|-65536|0|540.0|800.0|540.0|740.0>
	 * 
	 * @param shapeString
	 * @return
	 */
	public void parseString2ShapeList(String shapeString, int width, int height,int viewWidth,int viewHeight) {
		if (TextUtils.isEmpty(shapeString)) {
			return;
		}
        shapeString.trim();
        if(shapeString.length()<1){
			return;
		}

		int finalWidth = viewWidth;
		int finalHeight = finalWidth  * height / width;
		if (finalHeight > viewHeight) {
			finalHeight = viewHeight;
			finalWidth = finalHeight * width / height;
		}

		int leftDiff = (viewWidth - finalWidth) / 2;
		int topDiff = (viewHeight - finalHeight) / 2;

		LogUtil.d(TAG, "parseShapeList2String"
				+ " video size " + width + "x" + height
				+ " view size " + viewWidth + "x" + viewHeight
				+ " final size " + finalWidth + "x" + finalHeight
				+ " offset " + leftDiff + "x" + topDiff);

			shapeList = new ArrayList<Shape>();
		String[] allShapeData = shapeString.split(",");
		if (allShapeData != null && allShapeData.length > 0) {
			for (int i = 0; i < allShapeData.length; i++) {
				String tmp;
				if(allShapeData[i].contains("<")){
					tmp = allShapeData[i].substring(1, allShapeData[i].length() - 1);
				}else {
					tmp = allShapeData[i].substring(0, allShapeData[i].length());
				}
				String[] shapeData = tmp.split("\\|");
				if (shapeData != null) {
					int type = Integer.valueOf(shapeData[0]);
					if (type == 0) {
						// line
						Line line = new Line();
						line.type = Integer.valueOf(shapeData[0]);
						line.shapeColor = Integer.valueOf(shapeData[1]);
						line.isHidden = Integer.valueOf(shapeData[2]);
						if (width != 0) {
							line.startX = Float.valueOf(shapeData[3]) / (float) width * finalWidth + leftDiff;
							LogUtil.d(TAG, "startX " + shapeData[3] + " ==> " + line.startX);
							line.endX = Float.valueOf(shapeData[5]) / (float) width * finalWidth + leftDiff;
							LogUtil.d(TAG, "endX " + shapeData[5] + " ==> " + line.endX);

						} else {
							line.startX = Float.valueOf(shapeData[3]);
							line.endX = Float.valueOf(shapeData[5]);
						}

						if (height != 0) {
							line.startY = Float.valueOf(shapeData[4]) / (float) height * finalHeight + topDiff;
							LogUtil.d(TAG, "startY " + shapeData[4] + " ==> " + line.startY);
							line.endY = Float.valueOf(shapeData[6]) / (float) height * finalHeight + topDiff;
							LogUtil.d(TAG, "endY " + shapeData[6] + " ==> " + line.endY);

						} else {
							line.startY = Float.valueOf(shapeData[4]);

							line.endY = Float.valueOf(shapeData[6]);
						}
						shapeList.add(line);
					} else if (type == 2) {
						// circle
						Circle circle = new Circle();
						circle.type = Integer.valueOf(shapeData[0]);
						circle.shapeColor = Integer.valueOf(shapeData[1]);
						circle.isHidden = Integer.valueOf(shapeData[2]);
						circle.centerX = Float.valueOf(shapeData[3]) / (float) width * finalWidth + leftDiff;
						LogUtil.d(TAG, "centerX " + shapeData[3] + " ==> " + circle.centerX);
						circle.centerY = Float.valueOf(shapeData[4]) / (float) height * finalHeight + topDiff;
						LogUtil.d(TAG, "centerY " + shapeData[4] + " ==> " + circle.centerY);
						circle.endX = Float.valueOf(shapeData[5]) / (float) width * finalWidth + leftDiff;
						LogUtil.d(TAG, "endX " + shapeData[5] + " ==> " + circle.endX);
						circle.endY = Float.valueOf(shapeData[6]) / (float) height * finalHeight + topDiff;
						LogUtil.d(TAG, "endY " + shapeData[6] + " ==> " + circle.endY);

						circle.radius = Math.abs(circle.centerY - circle.endY);
						shapeList.add(circle);
					}
				}
			}
		}
	}

	/**
	 * convert all shape coordinate
	 */
	public void convertCoordinate(int dx, int dy) {
		if (shapeList != null && shapeList.size() > 0) {
			for (int i = 0; i < shapeList.size(); i++) {
				Shape shape = shapeList.get(i);
				if (shape.type == 0) {
					// line
					Line line = (Line) shape;
					line.startX = line.startX + dx;
					line.startY = line.startY + dy;
					line.endX = line.endX + dx;
					line.endY = line.endY + dy;
				} else if (shape.type == 2) {
					// circle
					Circle circle = (Circle) shape;
					circle.centerX = circle.centerX + dx;
					circle.centerY = circle.centerY + dy;
				}
			}
		}
	}

	public int getShapeNum() {
		if (shapeList != null)
			return shapeList.size();
		return 0;
	}

	public void updateDraw() {
		postInvalidate();
	}

	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
		if (!isFocus) {
			Draw();
		}
	}
}