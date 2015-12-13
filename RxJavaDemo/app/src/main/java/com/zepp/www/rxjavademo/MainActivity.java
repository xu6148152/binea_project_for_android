package com.zepp.www.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getCanonicalName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //observableType1();
        //observableType2();
        //observableType3();
        observableType4();
    }

    private void observableType4() {
        String[] s = {"url1", "url2", "url3"};
        Observable.from(s).subscribe(new Action1<String>() {
            @Override public void call(String s) {
                Log.d(TAG, " observableType4 " + s);
            }
        });
    }

    private void observableType3() {
        Observable.just("hello world").map(new Func1<String, String>() {
            @Override public String call(String s) {
                return s + " xu";
            }
        }).subscribe(new Action1<String>() {

            @Override public void call(String s) {
                Log.d(TAG, " observableType3 " + s);
            }
        });
    }

    private void observableType2() {
        Observable.just("hello world").subscribe(new Action1<String>() {
            @Override public void call(String s) {
                Log.d(TAG, " observableType2 " + s);
            }
        });
    }

    private void observableType1() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello world");
                subscriber.onCompleted();
            }
        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(String s) {
                Log.i(TAG, s);
            }
        };

        observable.subscribe(subscriber);
    }
}
