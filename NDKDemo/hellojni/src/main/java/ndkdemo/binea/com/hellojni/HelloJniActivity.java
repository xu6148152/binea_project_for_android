package ndkdemo.binea.com.hellojni;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by xubinggui on 1/31/16.
 */
public class HelloJniActivity extends Activity {

    private final String TAG = getClass().getSimpleName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvHello = (TextView) findViewById(R.id.tv_content);
        //tvHello.setText(stringFromJNI());
    }

    ///* A native method that is implemented by the
    //* 'hello-jni' native library, which is packaged
    //* with this application.
    //*/
    //public native String  stringFromJNI();
    //
    ///* This is another native method declaration that is *not*
    // * implemented by 'hello-jni'. This is simply to show that
    // * you can declare as many native methods in your Java code
    // * as you want, their implementation is searched in the
    // * currently loaded native libraries only the first time
    // * you call them.
    // *
    // * Trying to call this function will result in a
    // * java.lang.UnsatisfiedLinkError exception !
    // */
    //public native String  unimplementedStringFromJNI();
    //
    ///* this is used to load the 'hello-jni' library on application
    // * startup. The library has already been unpacked into
    // * /data/data/com.example.hellojni/lib/libhello-jni.so at
    // * installation time by the package manager.
    // */
    //static {
    //    System.loadLibrary("hellojni");
    //}
}
