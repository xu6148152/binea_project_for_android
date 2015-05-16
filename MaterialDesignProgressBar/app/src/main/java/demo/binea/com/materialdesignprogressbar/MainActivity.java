package demo.binea.com.materialdesignprogressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import demo.binea.com.materialdesignprogressbar.widget.MaterialDesignProgressBar;


public class MainActivity extends ActionBarActivity {

	MaterialDesignProgressBar progressView;
	Thread updateThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressView = (MaterialDesignProgressBar) findViewById(R.id.progress_view);

		// Test loading animation
		startAnimationThreadStuff(1000);

		final Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (progressView.isIndeterminate()) {
					progressView.setIndeterminate(false);
					button.setText("Switch to indeterminate");
				} else {
					progressView.setIndeterminate(true);
					button.setText("Switch to determinate");
				}
				startAnimationThreadStuff(0);
			}
		});

	}

	private void startAnimationThreadStuff(long delay)
	{
		if(updateThread != null && updateThread.isAlive())
			updateThread.interrupt();
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Start animation after a delay so there's no missed frames while the app loads up
				progressView.setProgress(0f);
				progressView.startAnimation(); // Alias for resetAnimation, it's all the same
				// Run thread to update progress every half second until full
				updateThread = new Thread(new Runnable() {
					@Override
					public void run() {
						while (progressView.getProgress() < progressView.getMaxProgress() && !Thread.interrupted()) {
							// Must set progress in UI thread
							handler.post(new Runnable() {
								@Override
								public void run() {
									progressView.setProgress(progressView.getProgress() + 10);
								}
							});
							SystemClock.sleep(250);
						}
					}
				});
				updateThread.start();
			}
		}, delay);
	}
}
