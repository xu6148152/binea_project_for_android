package com.example.xubinggui.customviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.xubinggui.customviewtest.activity.AnimListActivity;
import com.example.xubinggui.customviewtest.activity.BitmapMeshView2Activity;
import com.example.xubinggui.customviewtest.activity.BitmapMeshViewActivity;
import com.example.xubinggui.customviewtest.activity.ClockViewActivity;
import com.example.xubinggui.customviewtest.activity.ColorfilterViewActivity;
import com.example.xubinggui.customviewtest.activity.CustomImageViewActivity;
import com.example.xubinggui.customviewtest.activity.CustomLayoutActivity;
import com.example.xubinggui.customviewtest.activity.DrawableActivity;
import com.example.xubinggui.customviewtest.activity.DreamEffectActivity;
import com.example.xubinggui.customviewtest.activity.ECGActivity;
import com.example.xubinggui.customviewtest.activity.FontViewActivity;
import com.example.xubinggui.customviewtest.activity.LightColorMatrixActivity;
import com.example.xubinggui.customviewtest.activity.MaskFilterActivity;
import com.example.xubinggui.customviewtest.activity.MatrixImageViewActivity;
import com.example.xubinggui.customviewtest.activity.MatrixViewActivity;
import com.example.xubinggui.customviewtest.activity.MutiplViewActivity;
import com.example.xubinggui.customviewtest.activity.PathEffectActivity;
import com.example.xubinggui.customviewtest.activity.PolylineViewActivity;
import com.example.xubinggui.customviewtest.activity.PorterDuffColorFilterActivity;
import com.example.xubinggui.customviewtest.activity.ReflectViewActivity;
import com.example.xubinggui.customviewtest.activity.ShaderViewActivity;
import com.example.xubinggui.customviewtest.activity.WaveViewActivity;
import com.example.xubinggui.customviewtest.activity.XfermodeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {
//	@InjectView(R.id.btn_circle_view)
//	Button btn_circle_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
	}

	@OnClick(R.id.btn_circle_view)
	public void goCircleView(){
		startActivity(new Intent(this, ColorfilterViewActivity.class));
	}

	@OnClick(R.id.btn_light_matrix)
	public void goLightMatrix(){
		startActivity(new Intent(this, LightColorMatrixActivity.class));
	}

	@OnClick(R.id.btn_porter_duff_color_filter)
	public void goPorterDuffFilter(){
		startActivity(new Intent(this, PorterDuffColorFilterActivity.class));
	}

	@OnClick(R.id.btn_avoid_fermode)
	public void goAvoidXfermode(){
		startActivity(new Intent(this, XfermodeActivity.class));
	}

	@OnClick(R.id.btn_clock_view)
	public void goClockView(){
		startActivity(new Intent(this, ClockViewActivity.class));
	}
	@OnClick(R.id.btn_mutipl_view)
	public void multiplView(){
		startActivity(new Intent(this, MutiplViewActivity.class));
	}
	@OnClick(R.id.btn_font_view)
	public void goFontView(){
		startActivity(new Intent(this, FontViewActivity.class));
	}

	@OnClick(R.id.btn_mask_filter_view)
	public void goMaskFilterView(){
		startActivity(new Intent(this, MaskFilterActivity.class));
	}
	@OnClick(R.id.btn_path_effect_view)
	public void goPathEffectView(){
		startActivity(new Intent(this, PathEffectActivity.class));
	}
	@OnClick(R.id.btn_ecg_view)
	public void goECGView(){
		startActivity(new Intent(this, ECGActivity.class));
	}
	@OnClick(R.id.btn_shader_view)
	public void goShaderView(){
		startActivity(new Intent(this, ShaderViewActivity.class));
	}
	@OnClick(R.id.btn_reflect_view)
	public void goReflectView(){
		startActivity(new Intent(this, ReflectViewActivity.class));
	}
	@OnClick(R.id.btn_dream_effect_view)
	public void goDreamEffect(){
		startActivity(new Intent(this, DreamEffectActivity.class));
	}
	@OnClick(R.id.btn_matrix_view)
	public void goMatrixView(){
		startActivity(new Intent(this, MatrixViewActivity.class));
	}
	@OnClick(R.id.btn_matrix_image_view)
	public void goMatrixImageView(){
		startActivity(new Intent(this, MatrixImageViewActivity.class));
	}
	@OnClick(R.id.btn_anim_list_view)
	public void goAnimListView(){
		startActivity(new Intent(this, AnimListActivity.class));
	}
	@OnClick(R.id.btn_mesh_view)
	public void goMeshView(){
		startActivity(new Intent(this, BitmapMeshViewActivity.class));
	}
	@OnClick(R.id.btn_mesh_view2)
	public void goMeshView2(){
		startActivity(new Intent(this, BitmapMeshView2Activity.class));
	}
	@OnClick(R.id.btn_wave_view)
	public void goWaveView(){
		startActivity(new Intent(this, WaveViewActivity.class));
	}
	@OnClick(R.id.btn_polyline_view)
	public void goPolyLineView(){
		startActivity(new Intent(this, PolylineViewActivity.class));
	}
	@OnClick(R.id.btn_custom_image_view)
	public void goCustomImageView(){
		startActivity(new Intent(this, CustomImageViewActivity.class));
	}
	@OnClick(R.id.btn_custom_layout)
	public void goCustomLayout(){
		startActivity(new Intent(this, CustomLayoutActivity.class));
	}
	@OnClick(R.id.btn_drawable)
	public void goDrawable(){
		startActivity(new Intent(this, DrawableActivity.class));
	}
}
