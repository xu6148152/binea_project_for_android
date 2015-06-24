package com.example.xubinggui.customviewtest.activity;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.xubinggui.customviewtest.R;
import com.example.xubinggui.customviewtest.widget.PolylineView;

import java.util.ArrayList;
import java.util.List;

public class PolylineViewActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_polyline_view);

		PolylineView mPolylineView = (PolylineView) findViewById(R.id.poly_line_view);

		List<PointF> pointFs = new ArrayList<PointF>();
		pointFs.add(new PointF(0.3F, 0.5F));
		pointFs.add(new PointF(1F, 2.7F));
		pointFs.add(new PointF(2F, 3.5F));
		pointFs.add(new PointF(3F, 3.2F));
		pointFs.add(new PointF(4F, 1.8F));
		pointFs.add(new PointF(5F, 1.5F));
		pointFs.add(new PointF(6F, 2.2F));
		pointFs.add(new PointF(7F, 5.5F));
		pointFs.add(new PointF(8F, 7F));
		pointFs.add(new PointF(8.6F, 5.7F));

		mPolylineView.setData(pointFs, "Money", "Time");
	}
}
