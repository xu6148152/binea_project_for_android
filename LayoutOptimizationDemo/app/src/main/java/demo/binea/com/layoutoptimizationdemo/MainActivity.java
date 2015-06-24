package demo.binea.com.layoutoptimizationdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewStub;


public class MainActivity extends ActionBarActivity {

	private boolean isInflate = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onTest(View view) {
		//manner 1.
		if(!isInflate) {
//			findViewById(R.id.stub_import).setVisibility(View.VISIBLE);
			isInflate = true;
			//manner 2.
		((ViewStub)findViewById(R.id.stub_import)).inflate();
		}



	}
}
