package com.example.xubinggui.adtest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;

import cn.waps.AppConnect;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AdManager.getInstance(this).init("68b1873bb6ef8cbf", "d088eb484463c693", true);
		AppConnect.getInstance(this).getPopAdView(this);

		SpotManager.getInstance(this).loadSpotAds();

		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);

		// 获取要嵌入广告条的布局
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);

		// 将广告条加入到布局中
		adLayout.addView(adView);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppConnect.getInstance(this).close();
		SpotManager.getInstance(this)
				.unregisterSceenReceiver();
	}

	public void showAdBoard(View view){
		AppConnect.getInstance(this).showOffers(this);
	}
}
