package com.example.android.bluetoothchat;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by xubinggui on 8/5/15.
 */
public class DialogUtil {
    public static void showDialog(final Context context) {
        final InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        final Dialog alBuilder = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.rename_dialog, null);
        //alBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alBuilder.setTitle("Rename");
        Button change = (Button) view.findViewById(R.id.change);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        final EditText editText = (EditText) view.findViewById(R.id.et_new_file_name);
        change.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String newFileName = editText.getText().toString();
                if (TextUtils.isEmpty(newFileName)) {
                    Toast.makeText(context, "new name can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                final boolean isRename = FileUtil.renameFile(newFileName + ".txt");
                Log.d("rename", "isRename " + isRename);
                if(!isRename){
                    Toast.makeText(context, "rename fail", Toast.LENGTH_SHORT).show();
                }

                alBuilder.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                final boolean isRename = FileUtil.renameFile(System.currentTimeMillis() + ".txt");
                Log.d("rename", "isRename " + isRename);
                alBuilder.dismiss();
            }
        });
        alBuilder.show();
        alBuilder.setContentView(view);
    }
}
