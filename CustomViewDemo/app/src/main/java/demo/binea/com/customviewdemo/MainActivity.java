package demo.binea.com.customviewdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button btn_01 = (Button) findViewById(R.id.btn_01);

		new Thread(new Runnable() {
			@Override
			public void run() {
				btn_01.setText("test");
			}
		}).start();
	}
}
