package com.zepp.www.likeanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zepp.www.likeanimation.leonids.ParticleSystem;

public class MainActivity extends AppCompatActivity {

    private SmallBang mSmallBang;
    private ImageView mImage;
    private Button mButton;
    private TextView mText;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSmallBang = SmallBang.attach2Window(this);

        mImage = (ImageView)findViewById(R.id.image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like(v);
            }
        });


        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(v);
            }
        });

        mText = (TextView) findViewById(R.id.text);
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redText(v);
            }
        });
    }

    public void addNumber(View view){
        mSmallBang.bang(view,new SmallBangListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                toast("button +1");
            }
        });
    }

    public void redText(View view){
        mText.setTextColor(0xFFCD8BF8);
        mSmallBang.bang(view,50,new SmallBangListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                toast("text+1");
            }
        });
    }

    public void like(View view){
        mImage.setImageResource(R.mipmap.ic_star_rate_on);
        mSmallBang.bang(view);
        mSmallBang.setSmallBangListener(new SmallBangListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                toast("heart+1");
            }
        });
    }

    private void toast(String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    public void goPraticale(View view) {
        ParticleSystem ps = new ParticleSystem(this, 100, R.mipmap.ic_launcher, 800);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedRange(0.1f, 0.25f);
        ps.setAcceleration(0.0001f, 90);
        ps.setRotationSpeedRange(90, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(view, 100);
    }
}
