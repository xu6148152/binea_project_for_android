package demo.binea.com.openglesdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import demo.binea.com.openglesdemo.model.Square;


public class MainActivity extends ActionBarActivity {

	private static String effectType;
	private GLSurfaceView mView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		mView = new GLSurfaceView(this);
		setContentView(mView);

		mView.setEGLContextClientVersion(2);
//		view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		mView.setRenderer(new EffectsRenderer(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("" + EffectFactory.EFFECT_AUTOFIX).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				effectType = EffectFactory.EFFECT_AUTOFIX;
				mView.requestRender();
				return true;
			}
		});
		menu.add("" + EffectFactory.EFFECT_BACKDROPPER).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				effectType = EffectFactory.EFFECT_BACKDROPPER;
				mView.requestRender();
				return true;
			}
		});
		menu.add("" + EffectFactory.EFFECT_BITMAPOVERLAY).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				effectType = EffectFactory.EFFECT_BITMAPOVERLAY;
				mView.requestRender();
				return true;
			}
		});
		menu.add("" + EffectFactory.EFFECT_BLACKWHITE).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				effectType = EffectFactory.EFFECT_BLACKWHITE;
				mView.requestRender();
				return true;
			}
		});
		menu.add("" + EffectFactory.EFFECT_BRIGHTNESS).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				effectType = EffectFactory.EFFECT_BRIGHTNESS;
				mView.requestRender();
				return true;
			}
		});
		menu.add("" + EffectFactory.EFFECT_CONTRAST).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				effectType = EffectFactory.EFFECT_CONTRAST;
				mView.requestRender();
				return true;
			}
		});
		menu.add("" + EffectFactory.EFFECT_CROP).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				effectType = EffectFactory.EFFECT_CROP;
				mView.requestRender();
				return true;
			}
		});
		menu.add("" + EffectFactory.EFFECT_CROSSPROCESS).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				effectType = EffectFactory.EFFECT_CROSSPROCESS;
				mView.requestRender();
				return true;
			}
		});
		return true;
	}

	private static class EffectsRenderer implements GLSurfaceView.Renderer{

		private Bitmap photo;
		private int photoWidth, photoHeight;

		private int textures[] = new int[2];
		private Square square;

		private EffectContext effectContext;
		private Effect effect;

		private void generateSquare() {
			GLES20.glGenTextures(2, textures, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, photo, 0);
			square = new Square();
		}

		public EffectsRenderer(Context context){
			photo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.home_training_center);
			photoWidth = photo.getWidth();
			photoHeight = photo.getHeight();
		}

		@Override
		public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

		}

		@Override
		public void onSurfaceChanged(GL10 gl10, int width, int height) {
			GLES20.glViewport(0,0,width, height);
			GLES20.glClearColor(0,0,0,1);
			generateSquare();
		}

		@Override
		public void onDrawFrame(GL10 gl10) {
			if(effectContext==null) {
				effectContext = EffectContext.createWithCurrentGlContext();
			}
			if(effect!=null){
				effect.release();
			}
			if(null == effectType){
				effectType = EffectFactory.EFFECT_DOCUMENTARY;
			}
			grayScaleEffect(effectType);
			square.draw(textures[1]);
		}

		private void grayScaleEffect(String effectType){
			EffectFactory factory = effectContext.getFactory();
			effect = factory.createEffect(effectType);
			effect.apply(textures[0], photoWidth, photoHeight, textures[1]);
		}
	}
}
