package demo.binea.com.heatmapdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import demo.binea.com.heatmapdemo.utils.RacquetHeatmap;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageView iv_heat_map = (ImageView) findViewById(R.id.iv_heat_map);
		Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.racket_base_middle);
		Rect rect = new Rect(78, 12, 234, 234);
		float[][] temp = new float[1][2];
		temp[0][0] = 0.0f;
		temp[0][1] = 0.0f;
		final Bitmap bitmap = RacquetHeatmap.generate(bg, rect, new RacquetHeatmap.Ellipse(104, 72), temp, Color.parseColor("#EAFF00"), 0, true, 24);

		iv_heat_map.setImageBitmap(bitmap);
	}
}
