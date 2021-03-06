package demo.binea.com.dragtoplayout.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import de.greenrobot.event.EventBus;
import demo.binea.com.dragtoplayout.R;
import demo.binea.com.dragtoplayout.Util.AttachUtil;

/**
 * Created by xubinggui on 5/23/15.
 */
public class WebViewFragment extends Fragment {

	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_webview, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
	}

	private void initView(){
		webView = (WebView) getView().findViewById(R.id.web_view);
		webView.loadUrl("http://search.yahoo.com/");

		// webView does not have scroll listener
		webView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				EventBus.getDefault().post(AttachUtil.isWebViewAttach(webView));
				return false;
			}
		});



	}
}
