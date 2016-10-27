package com.zepp.www.shortcutdemo;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getCanonicalName();

    private ShortcutManager mShortcutManager;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        mShortcutManager = getSystemService(ShortcutManager.class);

        ShortcutManager shortcutManager = mShortcutManager;

        ShortcutInfo webShortCut = new ShortcutInfo.Builder(this, "shortcut_web").setShortLabel("xu6148152 github")
                                                                                 .setLongLabel("Open xu6148152 github website")
                                                                                 .setIcon(Icon.createWithResource(this,
                                                                                                                  R.drawable.ic_dynamic_shortcut))
                                                                                 .setIntent(new Intent(Intent.ACTION_VIEW,
                                                                                                       Uri.parse("https://github.com/xu6148152")))
                                                                                 .build();

        ShortcutInfo dynamicShortcut = new ShortcutInfo.Builder(this, "shortcut_dynamic").setShortLabel("Dynamic")
                                                                                         .setLongLabel("Open dynamic shortcut")
                                                                                         .setIcon(Icon.createWithResource(this,
                                                                                                                          R.drawable.ic_dynamic_shortcut_2))
                                                                                         .setIntents(new Intent[] {
                                                                                                 new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this,
                                                                                                            MainActivity.class).setFlags(
                                                                                                         Intent.FLAG_ACTIVITY_CLEAR_TASK),
                                                                                                 new Intent(DynamicShortcutActivity.ACTION.ACTION)
                                                                                         })
                                                                                         .build();

        shortcutManager.setDynamicShortcuts(Arrays.asList(webShortCut, dynamicShortcut));
    }

    @Override protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public void changeShortcutOrder(View view) {

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(android.R.color.holo_red_dark, getTheme()));
        String label = "https://github.com/xu6148152";
        SpannableStringBuilder coloredLabel = new SpannableStringBuilder(label);
        coloredLabel.setSpan(colorSpan, 0, label.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        ShortcutInfo webShortCut = new ShortcutInfo.Builder(this, "shortcut_web").setShortLabel(coloredLabel).setRank(1).build();
        ShortcutInfo dynamicShortcut = new ShortcutInfo.Builder(MainActivity.this, "shortcut_dynamic").setRank(0).build();

        mShortcutManager.updateShortcuts(Arrays.asList(webShortCut, dynamicShortcut));
    }
}
