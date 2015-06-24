package demo.binea.com.beautyprogressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBarActivity;

import demo.binea.com.beautyprogressbar.widget.HorizontalProgressBar;


public class MainActivity extends ActionBarActivity {

	private HorizontalProgressBar mProgressBar;
	private static final int MSG_PROGRESS_UPDATE = 0x110;

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
				int progress = mProgressBar.getProgress();
				mProgressBar.setProgress(++progress);
				if (progress >= 100) {
					mHandler.removeMessages(MSG_PROGRESS_UPDATE);

				}
				mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mProgressBar = (HorizontalProgressBar) findViewById(R.id.id_progressbar01);
		mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);

		ContentLoadingProgressBar content_progress_bar = (ContentLoadingProgressBar) findViewById(R.id.content_progress_bar);
		content_progress_bar.show();

	}
}
