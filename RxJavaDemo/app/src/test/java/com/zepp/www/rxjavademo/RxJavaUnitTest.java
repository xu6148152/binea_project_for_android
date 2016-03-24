package com.zepp.www.rxjavademo;

import android.util.Log;
import org.junit.Test;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xubinggui on 2/20/16.
 */
public class RxJavaUnitTest {

    public final String TAG = getClass().getCanonicalName();

    @Test public void observableType4() {
        String[] s = { "url1", "url2", "url3" };
        Observable.from(s).subscribe(new Action1<String>() {
            @Override public void call(String s) {
                System.out.print(" observableType4 " + s);
            }
        });
    }

    @Test public void observableType3() {
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

    @Test public void observableType2() {
        Observable.just("hello world").subscribe(new Action1<String>() {
            @Override public void call(String s) {
                Log.d(TAG, " observableType2 " + s);
            }
        });
    }

    @Test public void observableType1() {
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

    @Test public void observableType5() {
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        });

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override public void onCompleted() {
                System.out.print("onCompleted");
            }

            @Override public void onError(Throwable e) {
                System.out.print("onError " + e.getMessage());
            }

            @Override public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }
        };

        observable.subscribe(subscriber);
    }

    /**
     * Simplify type5
     */
    @Test public void observableType6() {
        Observable.just(1, 2, 3).subscribe(new Subscriber<Integer>() {
            @Override public void onCompleted() {
                System.out.print("onCompleted");
            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }
        });
    }

    /**
     * only emit odd number.
     */
    @Test public void observableType7() {
        Observable.just(1, 2, 3, 4, 5, 6).filter(new Func1<Integer, Boolean>() {
            @Override public Boolean call(Integer integer) {
                return integer % 2 == 1;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override public void onCompleted() {
                System.out.print("onCompleted");
            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }
        });
    }

    /**
     * Using map operator to calculate odd number square
     */
    @Test public void observableType8() {
        Observable.just(1, 2, 3, 4, 5, 6).filter(new Func1<Integer, Boolean>() {
            @Override public Boolean call(Integer integer) {
                return integer % 2 == 1;
            }
        }).map(new Func1<Integer, Object>() {
            @Override public Object call(Integer integer) {
                return Math.sqrt(integer);
            }
        }).subscribe(new Subscriber<Object>() {
            @Override public void onCompleted() {
                System.out.print("onCompleted");
            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onNext(Object o) {
                System.out.println("onNext " + o);
            }
        });
    }

    private String longRunningOperation() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // error
        }
        return "Complete!";
    }

    @Test
    public void observableType9() {
        final Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(longRunningOperation());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());

        observable.subscribe(new Action1<String>() {
            @Override public void call(String s) {
                System.out.println("call " + s);
            }
        });
    }

    @Test
    public void observableType10() {
        Subscription subscription = Single.create(new Single.OnSubscribe<Object>() {
            @Override public void call(SingleSubscriber<? super Object> singleSubscriber) {
                String value = longRunningOperation();
                singleSubscriber.onSuccess(value);
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Action1<Object>() {
            @Override public void call(Object o) {

            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {

            }
        });
    }
}
