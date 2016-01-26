package ndkdemo.binea.com.ndkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import com.zepp.www.moreteapots.moreteapots.MoreTeaPotsNativeActivity;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	public native String hellojni();
	static{
		System.loadLibrary("hellojni");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tv = (TextView) findViewById(R.id.tv);
		tv.setText(hellojni());
//		Log.i(TAG,hellojni());
	}

	public void moreTeaPots(View view) {
		startActivity(new Intent(this, MoreTeaPotsNativeActivity.class));
	}
}
