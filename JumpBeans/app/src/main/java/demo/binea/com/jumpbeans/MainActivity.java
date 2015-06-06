package demo.binea.com.jumpbeans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import demo.binea.com.jumpbeans.widget.JumpingBeans;

public class MainActivity extends AppCompatActivity {

	private JumpingBeans jumpingBeans1;
	private JumpingBeans jumpingBeans2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Here you can see that we don't duplicate trailing dots on the text (we reuse
		// them or, if it's an ellipsis character, replace it with three dots and animate
		// those instead)
		final TextView textView1 = (TextView) findViewById(R.id.jumping_text_1);
		jumpingBeans1 = JumpingBeans.with(textView1)
				.appendJumpingDots()
				.build();

		// Note that, even though we access textView2's text when starting to work on
		// the animation builder, we don't alter it in any way, so we're ok
//		final TextView textView2 = (TextView) findViewById(R.id.jumping_text_2);
//		jumpingBeans2 = JumpingBeans.with(textView2)
//				.makeTextJump(0, textView2.getText().toString().indexOf(' '))
//				.setIsWave(false)
//				.setLoopDuration(1000)
//				.build();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(null != jumpingBeans1){
			jumpingBeans1.stopJumping();
		}

		if(null != jumpingBeans2){
			jumpingBeans2.stopJumping();
		}
	}
}
