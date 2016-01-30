package bristol.zepp.com.databindingdemo;

import android.view.View;
import android.widget.Toast;

public class MyHandlers {
    public void onClickFriend(View view) {
        Toast.makeText(view.getContext(), "Friend", Toast.LENGTH_SHORT).show();
    }
    public void onClickEnemy(View view) {
        Toast.makeText(view.getContext(), "Enemy", Toast.LENGTH_SHORT).show();
    }
}