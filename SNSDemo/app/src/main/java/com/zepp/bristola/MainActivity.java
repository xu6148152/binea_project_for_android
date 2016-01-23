package com.zepp.bristola;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.binea.www.snsdemo.R;
import com.zepp.bristola.model.SocialInfo;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SNSManager.getInstance().registerWX(this, new SocialInfo());
    }

    public void loginWeiXin(View view) {
        SNSManager.getInstance().loginWeChat(this);
    }

    public void shareWeiXin(View view) {
        SNSManager.getInstance().shareWX();
    }

    public void loginQQ(View view) {
        SNSManager.getInstance().loginQQ(this);
    }

    public void shareQQ(View view) {

    }

    public void loginWeibo(View view) {

    }

    public void shareWeibo(View view) {

    }
}
