package ndkdemo.binea.com.ndkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void moreTeaPots(View view) {
        //startActivity(new Intent(this, MoreTeaPotsNativeActivity.class));
    }

    public void helloJni(View view) {
        //startActivity(new Intent(this, HelloJniActivity.class));
    }
}
