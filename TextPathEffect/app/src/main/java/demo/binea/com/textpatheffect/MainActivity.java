package demo.binea.com.textpatheffect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import demo.binea.com.textpatheffect.widget.TextPathEffect;


public class MainActivity extends ActionBarActivity {

	private TextPathEffect mPathTextView;
	private EditText mEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPathTextView = (TextPathEffect) findViewById(R.id.path);
		mEditText = (EditText) findViewById(R.id.edit);
		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPathTextView.setText(mEditText.getText().toString());
			}
		});
	}
}
