package demo.binea.com.turnpageview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xubinggui on 15/1/31.
 */
public class CurlView extends View {


	private static final float CURVATURE = 1 / 4F;// Çú¶ÈÖµ
	private static final float VALUE_ADDED = 1 / 400F;// ¾«¶È¸½¼ÓÖµÕ¼±È
	private static final float BUFF_AREA = 1 / 50F;// µ×²¿»º³åÇøÓòÕ¼±È
	private static final float AUTO_AREA_BUTTOM_RIGHT = 3 / 4F, AUTO_AREA_BUTTOM_LEFT = 1 / 8F;// ÓÒÏÂ½ÇºÍ×ó²à×Ô»¬ÇøÓòÕ¼±È
	private static final float AUTO_SLIDE_BL_V = 1 / 25F, AUTO_SLIDE_BR_V = 1 / 100F;// »¬¶¯ËÙ¶ÈÕ¼±È
	private static final float TEXT_SIZE_NORMAL = 1 / 40F, TEXT_SIZE_LARGER = 1 / 20F;// ±ê×¼ÎÄ×Ö³ß´çºÍ´óºÅÎÄ×Ö³ß´çµÄÕ¼±È

	private List<Bitmap> mBitmaps;// Î»Í¼Êý¾ÝÁÐ±í

	private SlideHandler mSlideHandler;// »¬¶¯´¦ÀíHandler
	private Paint mPaint;// »­±Ê
	private TextPaint mTextPaint;// ÎÄ±¾»­±Ê
	private Context mContext;// ÉÏÏÂÎÄ»·¾³ÒýÓÃ

	private Path mPath;// ÕÛµþÂ·¾¶
	private Path mPathFoldAndNext;// Ò»¸ö°üº¬ÕÛµþºÍÏÂÒ»Ò³ÇøÓòµÄPath
	private Path mPathSemicircleBtm, mPathSemicircleLeft;// µ×²¿ºÍ×ó±ßÔÂ°ëÔ²Path
	private Path mPathTrap;// ÌÝÐÎÇøÓòPath

	private Region mRegionShortSize;// ¶Ì±ßµÄÓÐÐ§ÇøÓò
	private Region mRegionCurrent;// µ±Ç°Ò³ÇøÓò£¬ÆäÊµ¾ÍÊÇ¿Ø¼þµÄ´óÐ¡
	private Region mRegionNext;// µ±Ç°Ò³ÇøÓò£¬ÆäÊµ¾ÍÊÇ¿Ø¼þµÄ´óÐ¡
	private Region mRegionFold;// µ±Ç°Ò³ÇøÓò£¬ÆäÊµ¾ÍÊÇ¿Ø¼þµÄ´óÐ¡
	private Region mRegionSemicircle;// Á½ÔÂ°ëÔ²ÇøÓò

	private int mViewWidth, mViewHeight;// ¿Ø¼þ¿í¸ß
	private int mPageIndex;// µ±Ç°ÏÔÊ¾mBitmapsÊý¾ÝµÄÏÂ±ê

	private float mPointX, mPointY;// ÊÖÖ¸´¥ÃþµãµÄ×ø±ê
	private float mValueAdded;// ¾«¶È¸½¼õÖµ
	private float mBuffArea;// µ×²¿»º³åÇøÓò
	private float mAutoAreaButtom, mAutoAreaRight, mAutoAreaLeft;// ÓÒÏÂ½ÇºÍ×ó²à×Ô»¬ÇøÓò
	private float mStart_X, mStart_Y;// Ö±ÏßÆðµã×ø±ê
	private float mAutoSlideV_BL, mAutoSlideV_BR;// »¬¶¯ËÙ¶È
	private float mTextSizeNormal, mTextSizeLarger;// ±ê×¼ÎÄ×Ö³ß´çºÍ´óºÅÎÄ×Ö³ß´ç
	private float mDegrees;// µ±Ç°Y±ß³¤ÓëYÖáµÄ¼Ð½Ç

	private boolean isSlide, isLastPage, isNextPage;// ÊÇ·ñÖ´ÐÐ»¬¶¯¡¢ÊÇ·ñÒÑµ½×îºóÒ»Ò³¡¢ÊÇ·ñ¿ÉÏÔÊ¾ÏÂÒ»Ò³µÄ±êÊ¶Öµ

	private Slide mSlide;// ¶¨Òåµ±Ç°»¬¶¯ÊÇÍù×óÏÂ»¬»¹ÊÇÓÒÏÂ»¬

	/**
	 * Ã¶¾ÙÀà¶¨Òå»¬¶¯·½Ïò
	 */
	private enum Slide {
		LEFT_BOTTOM, RIGHT_BOTTOM
	}

	private Ratio mRatio;// ¶¨Òåµ±Ç°ÕÛµþ±ß³¤

	/**
	 * Ã¶¾ÙÀà¶¨Òå³¤±ß¶Ì±ß
	 */
	private enum Ratio {
		LONG, SHORT
	}

	public CurlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		/*
		 * ÊµÀý»¯ÎÄ±¾»­±Ê²¢ÉèÖÃ²ÎÊý
		 */
		mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
		mTextPaint.setTextAlign(Paint.Align.CENTER);

		/*
		 * ÊµÀý»¯»­±Ê¶ÔÏó²¢ÉèÖÃ²ÎÊý
		 */
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);

		/*
		 * ÊµÀý»¯Â·¾¶¶ÔÏó
		 */
		mPath = new Path();
		mPathFoldAndNext = new Path();
		mPathSemicircleBtm = new Path();
		mPathSemicircleLeft = new Path();
		mPathTrap = new Path();

		/*
		 * ÊµÀý»¯ÇøÓò¶ÔÏó
		 */
		mRegionShortSize = new Region();
		mRegionCurrent = new Region();
		mRegionSemicircle = new Region();

		// ÊµÀý»¯»¬¶¯Handler´¦ÀíÆ÷
		mSlideHandler = new SlideHandler();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		/*
		 * »ñÈ¡¿Ø¼þ¿í¸ß
		 */
		mViewWidth = w;
		mViewHeight = h;

		// ³õÊ¼»¯Î»Í¼Êý¾Ý
		if (null != mBitmaps) {
			initBitmaps();
		}

		// ¼ÆËãÎÄ×Ö³ß´ç
		mTextSizeNormal = TEXT_SIZE_NORMAL * mViewHeight;
		mTextSizeLarger = TEXT_SIZE_LARGER * mViewHeight;

		// ¼ÆËã¾«¶È¸½¼ÓÖµ
		mValueAdded = mViewHeight * VALUE_ADDED;

		// ¼ÆËãµ×²¿»º³åÇøÓò
		mBuffArea = mViewHeight * BUFF_AREA;

		/*
		 * ¼ÆËã×Ô»¬Î»ÖÃ
		 */
		mAutoAreaButtom = mViewHeight * AUTO_AREA_BUTTOM_RIGHT;
		mAutoAreaRight = mViewWidth * AUTO_AREA_BUTTOM_RIGHT;
		mAutoAreaLeft = mViewWidth * AUTO_AREA_BUTTOM_LEFT;

		// ¼ÆËã¶Ì±ßµÄÓÐÐ§ÇøÓò
		computeShortSizeRegion();

		/*
		 * ¼ÆËã»¬¶¯ËÙ¶È
		 */
		mAutoSlideV_BL = mViewWidth * AUTO_SLIDE_BL_V;
		mAutoSlideV_BR = mViewWidth * AUTO_SLIDE_BR_V;

		// ¼ÆËãµ±Ç°Ò³ÇøÓò
		mRegionCurrent.set(0, 0, mViewWidth, mViewHeight);
	}

	/**
	 * ³õÊ¼»¯Î»Í¼Êý¾Ý
	 * Ëõ·ÅÎ»Í¼³ß´çÓëÆÁÄ»Æ¥Åä
	 */
	private void initBitmaps() {
		List<Bitmap> temp = new ArrayList<Bitmap>();
		for (int i = mBitmaps.size() - 1; i >= 0; i--) {
			Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), mViewWidth, mViewHeight, true);
			temp.add(bitmap);
		}
		mBitmaps = temp;
	}

	/**
	 * ¼ÆËã¶Ì±ßµÄÓÐÐ§ÇøÓò
	 */
	private void computeShortSizeRegion() {
		// ¶Ì±ßÔ²ÐÎÂ·¾¶¶ÔÏó
		Path pathShortSize = new Path();

		// ÓÃÀ´×°ÔØPath±ß½çÖµµÄRectF¶ÔÏó
		RectF rectShortSize = new RectF();

		// Ìí¼ÓÔ²ÐÎµ½Path
		pathShortSize.addCircle(0, mViewHeight, mViewWidth, Path.Direction.CCW);

		// ¼ÆËã±ß½ç
		pathShortSize.computeBounds(rectShortSize, true);

		// ½«Path×ª»¯ÎªRegion
		mRegionShortSize.setPath(pathShortSize, new Region((int) rectShortSize.left, (int) rectShortSize.top, (int) rectShortSize.right, (int) rectShortSize.bottom));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		/*
		 * Èç¹ûÊý¾ÝÎª¿ÕÔòÏÔÊ¾Ä¬ÈÏÌáÊ¾ÎÄ±¾
		 */
		if (null == mBitmaps || mBitmaps.size() == 0) {
			defaultDisplay(canvas);
			return;
		}

		// ÖØ»æÊ±ÖØÖÃÂ·¾¶
		mPath.reset();
		mPathFoldAndNext.reset();
		mPathTrap.reset();
		mPathSemicircleBtm.reset();
		mPathSemicircleLeft.reset();

		// »æÖÆµ×É«
		canvas.drawColor(Color.WHITE);

		/*
		 * Èç¹û×ø±êµãÔÚÔ­µã£¨¼´»¹Ã»·¢Éú´¥ÅöÊ±£©Ôò»æÖÆµÚÒ»Ò³
		 */
		if (mPointX == 0 && mPointY == 0) {
			canvas.drawBitmap(mBitmaps.get(mBitmaps.size() - 1), 0, 0, null);
			return;
		}

		/*
		 * ÅÐ¶Ï´¥ÃþµãÊÇ·ñÔÚ¶Ì±ßµÄÓÐÐ§ÇøÓòÄÚ
		 */
		if (!mRegionShortSize.contains((int) mPointX, (int) mPointY)) {
			// Èç¹û²»ÔÚÔòÍ¨¹ýx×ø±êÇ¿ÐÐÖØËãy×ø±ê
			mPointY = (float) (Math.sqrt((Math.pow(mViewWidth, 2) - Math.pow(mPointX, 2))) - mViewHeight);

			// ¾«¶È¸½¼ÓÖµ±ÜÃâ¾«¶ÈËðÊ§
			mPointY = Math.abs(mPointY) + mValueAdded;
		}

		/*
		 * »º³åÇøÓòÅÐ¶Ï
		 */
		float area = mViewHeight - mBuffArea;
		if (!isSlide && mPointY >= area) {
			mPointY = area;
		}

		/*
		 * ¶î£¬Õâ¸ö¸ÃÔõÃ´×¢ÊÍºÃÄØ¡­¡­¸ù¾ÝÍ¼À´
		 */
		float mK = mViewWidth - mPointX;
		float mL = mViewHeight - mPointY;

		// ÐèÒªÖØ¸´Ê¹ÓÃµÄ²ÎÊý´æÖµ±ÜÃâÖØ¸´¼ÆËã
		float temp = (float) (Math.pow(mL, 2) + Math.pow(mK, 2));

		/*
		 * ¼ÆËã¶Ì±ß³¤±ß³¤¶È
		 */
		float sizeShort = temp / (2F * mK);
		float sizeLong = temp / (2F * mL);

		float tempAM = mK - sizeShort;

		/*
		 * ¸ù¾Ý³¤¶Ì±ß±ß³¤¼ÆËãÐý×ª½Ç¶È²¢È·¶¨mRatioµÄÖµ
		 */
		if (sizeShort < sizeLong) {
			mRatio = Ratio.SHORT;
			float sin = tempAM / sizeShort;
			mDegrees = (float) (Math.asin(sin) / Math.PI * 180);
		} else {
			mRatio = Ratio.LONG;
			float cos = mK / sizeLong;
			mDegrees = (float) (Math.acos(cos) / Math.PI * 180);
		}

		if (sizeLong > mViewHeight) {
			// ¼ÆËã¡­¡­¶î¡­¡­°´Í¼À´AN±ß~
			float an = sizeLong - mViewHeight;

			// Èý½ÇÐÎAMNµÄMN±ß
			float largerTrianShortSize = an / (sizeLong - (mViewHeight - mPointY)) * (mViewWidth - mPointX);

			// Èý½ÇÐÎAQNµÄQN±ß
			float smallTrianShortSize = an / sizeLong * sizeShort;

			/*
			 * ¼ÆËã²ÎÊý
			 */
			float topX1 = mViewWidth - largerTrianShortSize;
			float topX2 = mViewWidth - smallTrianShortSize;
			float btmX2 = mViewWidth - sizeShort;

			// ¼ÆËãÇúÏßÆðµã
			float startXBtm = btmX2 - CURVATURE * sizeShort;
			float startYBtm = mViewHeight;

			// ¼ÆËãÇúÏßÖÕµã
			float endXBtm = mPointX + (1 - CURVATURE) * (tempAM);
			float endYBtm = mPointY + (1 - CURVATURE) * mL;

			// ¼ÆËãÇúÏß¿ØÖÆµã
			float controlXBtm = btmX2;
			float controlYBtm = mViewHeight;

			// ¼ÆËãÇúÏß¶¥µã
			float bezierPeakXBtm = 0.25F * startXBtm + 0.5F * controlXBtm + 0.25F * endXBtm;
			float bezierPeakYBtm = 0.25F * startYBtm + 0.5F * controlYBtm + 0.25F * endYBtm;

			/*
			 * Éú³É´øÇúÏßµÄËÄ±ßÐÎÂ·¾¶
			 */
			mPath.moveTo(startXBtm, startYBtm);
			mPath.quadTo(controlXBtm, controlYBtm, endXBtm, endYBtm);
			mPath.lineTo(mPointX, mPointY);
			mPath.lineTo(topX1, 0);
			mPath.lineTo(topX2, 0);

			/*
			 * Ìæ²¹ÇøÓòPath
			 */
			mPathTrap.moveTo(startXBtm, startYBtm);
			mPathTrap.lineTo(topX2, 0);
			mPathTrap.lineTo(bezierPeakXBtm, bezierPeakYBtm);
			mPathTrap.close();

			/*
			 * µ×²¿ÔÂ°ëÔ²Path
			 */
			mPathSemicircleBtm.moveTo(startXBtm, startYBtm);
			mPathSemicircleBtm.quadTo(controlXBtm, controlYBtm, endXBtm, endYBtm);
			mPathSemicircleBtm.close();

			/*
			 * Éú³É°üº¬ÕÛµþºÍÏÂÒ»Ò³µÄÂ·¾¶
			 */
			mPathFoldAndNext.moveTo(startXBtm, startYBtm);
			mPathFoldAndNext.quadTo(controlXBtm, controlYBtm, endXBtm, endYBtm);
			mPathFoldAndNext.lineTo(mPointX, mPointY);
			mPathFoldAndNext.lineTo(topX1, 0);
			mPathFoldAndNext.lineTo(mViewWidth, 0);
			mPathFoldAndNext.lineTo(mViewWidth, mViewHeight);
			mPathFoldAndNext.close();

			// ¼ÆËãÔÂ°ëÔ²ÇøÓò
			mRegionSemicircle = computeRegion(mPathSemicircleBtm);
		} else {
			/*
			 * ¼ÆËã²ÎÊý
			 */
			float leftY = mViewHeight - sizeLong;
			float btmX = mViewWidth - sizeShort;

			// ¼ÆËãÇúÏßÆðµã
			float startXBtm = btmX - CURVATURE * sizeShort;
			float startYBtm = mViewHeight;
			float startXLeft = mViewWidth;
			float startYLeft = leftY - CURVATURE * sizeLong;

			// ¼ÆËãÇúÏßÖÕµã
			float endXBtm = mPointX + (1 - CURVATURE) * (tempAM);
			float endYBtm = mPointY + (1 - CURVATURE) * mL;
			float endXLeft = mPointX + (1 - CURVATURE) * mK;
			float endYLeft = mPointY - (1 - CURVATURE) * (sizeLong - mL);

			// ¼ÆËãÇúÏß¿ØÖÆµã
			float controlXBtm = btmX;
			float controlYBtm = mViewHeight;
			float controlXLeft = mViewWidth;
			float controlYLeft = leftY;

			// ¼ÆËãÇúÏß¶¥µã
			float bezierPeakXBtm = 0.25F * startXBtm + 0.5F * controlXBtm + 0.25F * endXBtm;
			float bezierPeakYBtm = 0.25F * startYBtm + 0.5F * controlYBtm + 0.25F * endYBtm;
			float bezierPeakXLeft = 0.25F * startXLeft + 0.5F * controlXLeft + 0.25F * endXLeft;
			float bezierPeakYLeft = 0.25F * startYLeft + 0.5F * controlYLeft + 0.25F * endYLeft;

			/*
			 * ÏÞÖÆÓÒ²àÇúÏßÆðµã
			 */
			if (startYLeft <= 0) {
				startYLeft = 0;
			}

			/*
			 * ÏÞÖÆµ×²¿×ó²àÇúÏßÆðµã
			 */
			if (startXBtm <= 0) {
				startXBtm = 0;
			}

			/*
			 * ¸ù¾Ýµ×²¿×ó²àÏÞÖÆµãÖØÐÂ¼ÆËã±´Èû¶ûÇúÏß¶¥µã×ø±ê
			 */
			float partOfShortLength = CURVATURE * sizeShort;
			if (btmX >= -mValueAdded && btmX <= partOfShortLength - mValueAdded) {
				float f = btmX / partOfShortLength;
				float t = 0.5F * f;

				float bezierPeakTemp = 1 - t;
				float bezierPeakTemp1 = bezierPeakTemp * bezierPeakTemp;
				float bezierPeakTemp2 = 2 * t * bezierPeakTemp;
				float bezierPeakTemp3 = t * t;

				bezierPeakXBtm = bezierPeakTemp1 * startXBtm + bezierPeakTemp2 * controlXBtm + bezierPeakTemp3 * endXBtm;
				bezierPeakYBtm = bezierPeakTemp1 * startYBtm + bezierPeakTemp2 * controlYBtm + bezierPeakTemp3 * endYBtm;
			}

			/*
			 * ¸ù¾ÝÓÒ²àÏÞÖÆµãÖØÐÂ¼ÆËã±´Èû¶ûÇúÏß¶¥µã×ø±ê
			 */
			float partOfLongLength = CURVATURE * sizeLong;
			if (leftY >= -mValueAdded && leftY <= partOfLongLength - mValueAdded) {
				float f = leftY / partOfLongLength;
				float t = 0.5F * f;

				float bezierPeakTemp = 1 - t;
				float bezierPeakTemp1 = bezierPeakTemp * bezierPeakTemp;
				float bezierPeakTemp2 = 2 * t * bezierPeakTemp;
				float bezierPeakTemp3 = t * t;

				bezierPeakXLeft = bezierPeakTemp1 * startXLeft + bezierPeakTemp2 * controlXLeft + bezierPeakTemp3 * endXLeft;
				bezierPeakYLeft = bezierPeakTemp1 * startYLeft + bezierPeakTemp2 * controlYLeft + bezierPeakTemp3 * endYLeft;
			}

			/*
			 * Ìæ²¹ÇøÓòPath
			 */
			mPathTrap.moveTo(startXBtm, startYBtm);
			mPathTrap.lineTo(startXLeft, startYLeft);
			mPathTrap.lineTo(bezierPeakXLeft, bezierPeakYLeft);
			mPathTrap.lineTo(bezierPeakXBtm, bezierPeakYBtm);
			mPathTrap.close();

			/*
			 * Éú³É´øÇúÏßµÄÈý½ÇÐÎÂ·¾¶
			 */
			mPath.moveTo(startXBtm, startYBtm);
			mPath.quadTo(controlXBtm, controlYBtm, endXBtm, endYBtm);
			mPath.lineTo(mPointX, mPointY);
			mPath.lineTo(endXLeft, endYLeft);
			mPath.quadTo(controlXLeft, controlYLeft, startXLeft, startYLeft);

			/*
			 * Éú³Éµ×²¿ÔÂ°ëÔ²µÄPath
			 */
			mPathSemicircleBtm.moveTo(startXBtm, startYBtm);
			mPathSemicircleBtm.quadTo(controlXBtm, controlYBtm, endXBtm, endYBtm);
			mPathSemicircleBtm.close();

			/*
			 * Éú³ÉÓÒ²àÔÂ°ëÔ²µÄPath
			 */
			mPathSemicircleLeft.moveTo(endXLeft, endYLeft);
			mPathSemicircleLeft.quadTo(controlXLeft, controlYLeft, startXLeft, startYLeft);
			mPathSemicircleLeft.close();

			/*
			 * Éú³É°üº¬ÕÛµþºÍÏÂÒ»Ò³µÄÂ·¾¶
			 */
			mPathFoldAndNext.moveTo(startXBtm, startYBtm);
			mPathFoldAndNext.quadTo(controlXBtm, controlYBtm, endXBtm, endYBtm);
			mPathFoldAndNext.lineTo(mPointX, mPointY);
			mPathFoldAndNext.lineTo(endXLeft, endYLeft);
			mPathFoldAndNext.quadTo(controlXLeft, controlYLeft, startXLeft, startYLeft);
			mPathFoldAndNext.lineTo(mViewWidth, mViewHeight);
			mPathFoldAndNext.close();

			/*
			 * ¼ÆËãµ×²¿ºÍÓÒ²àÁ½ÔÂ°ëÔ²ÇøÓò
			 */
			Region regionSemicircleBtm = computeRegion(mPathSemicircleBtm);
			Region regionSemicircleLeft = computeRegion(mPathSemicircleLeft);

			// ºÏ²¢Á½ÔÂ°ëÔ²ÇøÓò
			mRegionSemicircle.op(regionSemicircleBtm, regionSemicircleLeft, Region.Op.UNION);
		}

		// ¸ù¾ÝPathÉú³ÉµÄÕÛµþÇøÓò
		mRegionFold = computeRegion(mPath);

		// Ìæ²¹ÇøÓò
		Region regionTrap = computeRegion(mPathTrap);

		// ÁîÕÛµþÇøÓòÓëÌæ²¹ÇøÓòÏà¼Ó
		mRegionFold.op(regionTrap, Region.Op.UNION);

		// ´ÓÏà¼ÓºóµÄÇøÓòÖÐÌÞ³ýµôÔÂ°ëÔ²µÄÇøÓò»ñµÃ×îÖÕÕÛµþÇøÓò
		mRegionFold.op(mRegionSemicircle, Region.Op.DIFFERENCE);

		/*
		 * ¼ÆËãÏÂÒ»Ò³ÇøÓò
		 */
		mRegionNext = computeRegion(mPathFoldAndNext);
		mRegionNext.op(mRegionFold, Region.Op.DIFFERENCE);

		drawBitmaps(canvas);
	}

	/**
	 * »æÖÆÎ»Í¼Êý¾Ý
	 *
	 * @param canvas
	 *            »­²¼¶ÔÏó
	 */
	private void drawBitmaps(Canvas canvas) {
		// »æÖÆÎ»Í¼Ç°ÖØÖÃisLastPageÎªfalse
		isLastPage = false;

		// ÏÞÖÆpageIndexµÄÖµ·¶Î§
		mPageIndex = mPageIndex < 0 ? 0 : mPageIndex;
		mPageIndex = mPageIndex > mBitmaps.size() ? mBitmaps.size() : mPageIndex;

		// ¼ÆËãÊý¾ÝÆðÊ¼Î»ÖÃ
		int start = mBitmaps.size() - 2 - mPageIndex;
		int end = mBitmaps.size() - mPageIndex;

		/*
		 * Èç¹ûÊý¾ÝÆðµãÎ»ÖÃÐ¡ÓÚ0Ôò±íÊ¾µ±Ç°ÒÑ¾­µ½ÁË×îºóÒ»ÕÅÍ¼Æ¬
		 */
		if (start < 0) {
			// ´ËÊ±ÉèÖÃisLastPageÎªtrue
			isLastPage = true;

			// ²¢ÏÔÊ¾ÌáÊ¾ÐÅÏ¢
			showToast("This is fucking lastest page");

			// Ç¿ÖÆÖØÖÃÆðÊ¼Î»ÖÃ
			start = 0;
			end = 1;
		}

		/*
		 * ¼ÆËãµ±Ç°Ò³µÄÇøÓò
		 */
		canvas.save();
		canvas.clipRegion(mRegionCurrent);
		canvas.drawBitmap(mBitmaps.get(end - 1), 0, 0, null);
		canvas.restore();

		/*
		 * ¼ÆËãÕÛµþÒ³µÄÇøÓò
		 */
		canvas.save();
		canvas.clipRegion(mRegionFold);

		canvas.translate(mPointX, mPointY);

		/*
		 * ¸ù¾Ý³¤¶Ì±ß±êÊ¶¼ÆËãÕÛµþÇøÓòÍ¼Ïñ
		 */
		if (mRatio == Ratio.SHORT) {
			canvas.rotate(90 - mDegrees);
			canvas.translate(0, -mViewHeight);
			canvas.scale(-1, 1);
			canvas.translate(-mViewWidth, 0);
		} else {
			canvas.rotate(-(90 - mDegrees));
			canvas.translate(-mViewWidth, 0);
			canvas.scale(1, -1);
			canvas.translate(0, -mViewHeight);
		}

		canvas.drawBitmap(mBitmaps.get(end - 1), 0, 0, null);
		canvas.restore();

		/*
		 * ¼ÆËãÏÂÒ»Ò³µÄÇøÓò
		 */
		canvas.save();
		canvas.clipRegion(mRegionNext);
		canvas.drawBitmap(mBitmaps.get(start), 0, 0, null);
		canvas.restore();
	}

	/**
	 * Ä¬ÈÏÏÔÊ¾
	 *
	 * @param canvas
	 *            Canvas¶ÔÏó
	 */
	private void defaultDisplay(Canvas canvas) {
		// »æÖÆµ×É«
		canvas.drawColor(Color.WHITE);

		// »æÖÆ±êÌâÎÄ±¾
		mTextPaint.setTextSize(mTextSizeLarger);
		mTextPaint.setColor(Color.RED);
		canvas.drawText("FBI WARNING", mViewWidth / 2, mViewHeight / 4, mTextPaint);

		// »æÖÆÌáÊ¾ÎÄ±¾
		mTextPaint.setTextSize(mTextSizeNormal);
		mTextPaint.setColor(Color.BLACK);
		canvas.drawText("Please set data use setBitmaps method", mViewWidth / 2, mViewHeight / 3, mTextPaint);
	}

	/**
	 * Í¨¹ýÂ·¾¶¼ÆËãÇøÓò
	 *
	 * @param path
	 *            Â·¾¶¶ÔÏó
	 * @return Â·¾¶µÄRegion
	 */
	private Region computeRegion(Path path) {
		Region region = new Region();
		RectF f = new RectF();
		path.computeBounds(f, true);
		region.setPath(path, new Region((int) f.left, (int) f.top, (int) f.right, (int) f.bottom));
		return region;
	}

	/**
	 * ¼ÆËã»¬¶¯²ÎÊý±ä»¯
	 */
	private void slide() {
		/*
		 * Èç¹û»¬¶¯±êÊ¶ÖµÎªfalseÔò·µ»Ø
		 */
		if (!isSlide) {
			return;
		}

		/*
		 * Èç¹ûµ±Ç°Ò³²»ÊÇ×îºóÒ»Ò³
		 * Èç¹ûÊÇÐèÒª·­ÏÂÒ»Ò³
		 * ²¢ÇÒÉÏÒ»Ò³ÒÑ±»×öµô
		 */
		if (!isLastPage && isNextPage && (mPointX - mAutoSlideV_BL <= -mViewWidth)) {
			mPointX = -mViewWidth;
			mPointY = mViewHeight;
			mPageIndex++;
			invalidate();
		}

		/*
		 * Èç¹ûµ±Ç°»¬¶¯±êÊ¶ÎªÏòÓÒÏÂ»¬¶¯x×ø±êºãÐ¡ÓÚ¿Ø¼þ¿í¶È
		 */
		else if (mSlide == Slide.RIGHT_BOTTOM && mPointX < mViewWidth) {
			// ÔòÈÃx×ø±ê×Ô¼Ó
			mPointX += mAutoSlideV_BR;

			// ²¢¸ù¾Ýx×ø±êµÄÖµÖØÐÂ¼ÆËãy×ø±êµÄÖµ
			mPointY = mStart_Y + ((mPointX - mStart_X) * (mViewHeight - mStart_Y)) / (mViewWidth - mStart_X);

			// ÈÃSlideHandler´¦ÀíÖØ»æ
			mSlideHandler.sleep(25);
		}

		/*
		 * Èç¹ûµ±Ç°»¬¶¯±êÊ¶ÎªÏò×óÏÂ»¬¶¯x×ø±êºã´óÓÚ¿Ø¼þ¿í¶ÈµÄ¸ºÖµ
		 */
		else if (mSlide == Slide.LEFT_BOTTOM && mPointX > -mViewWidth) {
			// ÔòÈÃx×ø±ê×Ô¼õ
			mPointX -= mAutoSlideV_BL;

			// ²¢¸ù¾Ýx×ø±êµÄÖµÖØÐÂ¼ÆËãy×ø±êµÄÖµ
			mPointY = mStart_Y + ((mPointX - mStart_X) * (mViewHeight - mStart_Y)) / (-mViewWidth - mStart_X);

			// ÈÃSlideHandler´¦ÀíÖØ»æ
			mSlideHandler.sleep(25);
		}
	}

	/**
	 * ÎªisSlideÌá¹©¶ÔÍâµÄÍ£Ö¹·½·¨±ãÓÚ±ØÒªÊ±ÊÍ·Å»¬¶¯¶¯»­
	 */
	public void slideStop() {
		isSlide = false;
	}

	/**
	 * Ìá¹©¶ÔÍâµÄ·½·¨»ñÈ¡ViewÄÚHandler
	 *
	 * @return mSlideHandler
	 */
	public SlideHandler getSlideHandler() {
		return mSlideHandler;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		isNextPage = true;

		/*
		 * »ñÈ¡µ±Ç°ÊÂ¼þµã
		 */
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_UP:// ÊÖÖ¸Ì§ÆðÊ±ºò
				if (isNextPage) {
				/*
				 * Èç¹ûµ±Ç°ÊÂ¼þµãÎ»ÓÚÓÒÏÂ×Ô»¬ÇøÓò
				 */
					if (x > mAutoAreaRight && y > mAutoAreaButtom) {
						// µ±Ç°ÎªÍùÓÒÏÂ»¬
						mSlide = Slide.RIGHT_BOTTOM;

						// Ä¦²Á°ÉÉ§Äê£¡
						justSlide(x, y);
					}

				/*
				 * Èç¹ûµ±Ç°ÊÂ¼þµãÎ»ÓÚ×ó²à×Ô»¬ÇøÓò
				 */
					if (x < mAutoAreaLeft) {
						// µ±Ç°ÎªÍù×óÏÂ»¬
						mSlide = Slide.LEFT_BOTTOM;

						// Ä¦²Á°ÉÉ§Äê£¡
						justSlide(x, y);
					}
				}
				break;
			case MotionEvent.ACTION_DOWN:
				isSlide = false;
			/*
			 * Èç¹ûÊÂ¼þµãÎ»ÓÚ»Ø¹öÇøÓò
			 */
				if (x < mAutoAreaLeft) {
					// ÄÇ¾Í²»·­ÏÂÒ»Ò³ÁË¶øÊÇÉÏÒ»Ò³
					isNextPage = false;
					mPageIndex--;
					mPointX = x;
					mPointY = y;
					invalidate();
				}
				downAndMove(event);
				break;
			case MotionEvent.ACTION_MOVE:
				downAndMove(event);
				break;
		}
		return true;
	}

	/**
	 * ´¦ÀíDOWNºÍMOVEÊÂ¼þ
	 *
	 * @param event
	 *            ÊÂ¼þ¶ÔÏó
	 */
	private void downAndMove(MotionEvent event) {
		if (!isLastPage) {
			mPointX = event.getX();
			mPointY = event.getY();

			invalidate();
		}
	}

	/**
	 * ÔÚÕâ¹â»¬µÄµØ°åÉÏ~
	 *
	 * @param x
	 *            µ±Ç°´¥Ãþµãx
	 * @param y
	 *            µ±Ç°´¥Ãþµãy
	 */
	private void justSlide(float x, float y) {
		// »ñÈ¡²¢ÉèÖÃÖ±Ïß·½³ÌµÄÆðµã
		mStart_X = x;
		mStart_Y = y;

		// OKÒª¿ªÊ¼»¬¶¯ÁËÅ¶~
		isSlide = true;

		// »¬¶¯
		slide();
	}

	/**
	 * ÉèÖÃÎ»Í¼Êý¾Ý
	 *
	 * @param bitmaps
	 *            Î»Í¼Êý¾ÝÁÐ±í
	 */
	public synchronized void setBitmaps(List<Bitmap> bitmaps) {
		/*
		 * Èç¹ûÊý¾ÝÎª¿ÕÔòÅ×³öÒì³£
		 */
		if (null == bitmaps || bitmaps.size() == 0)
			throw new IllegalArgumentException("no bitmap to display");

		/*
		 * Èç¹ûÊý¾Ý³¤¶ÈÐ¡ÓÚ2ÔòGGË¼ÃÜ´ï
		 */
		if (bitmaps.size() < 2)
			throw new IllegalArgumentException("fuck you and fuck to use imageview");

		mBitmaps = bitmaps;
		invalidate();
	}

	/**
	 * ToastÏÔÊ¾
	 *
	 * @param msg
	 *            ToastÏÔÊ¾ÎÄ±¾
	 */
	private void showToast(Object msg) {
		Toast.makeText(mContext, msg.toString(), Toast.LENGTH_SHORT).show();
	}

	/**
	 * ´¦Àí»¬¶¯µÄHandler
	 */
	@SuppressLint("HandlerLeak")
	public class SlideHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// Ñ­»·µ÷ÓÃ»¬¶¯¼ÆËã
			CurlView.this.slide();

			// ÖØ»æÊÓÍ¼
			CurlView.this.invalidate();
		}

		/**
		 * ÑÓ³ÙÏòHandler·¢ËÍÏûÏ¢ÊµÏÖÊ±¼ä¼ä¸ô
		 *
		 * @param delayMillis
		 *            ¼ä¸ôÊ±¼ä
		 */
		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	}
}
