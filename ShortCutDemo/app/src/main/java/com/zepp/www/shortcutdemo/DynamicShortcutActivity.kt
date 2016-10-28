package com.zepp.www.shortcutdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class DynamicShortcutActivity : AppCompatActivity() {

    object ACTION {
//        @JvmField val ACTION = "com.zepp.www.OPEN_DYNAMIC_SHORTCUT"
        const val ACTION = "com.zepp.www.OPEN_DYNAMIC_SHORTCUT"
    }

//    object TAG {
//        const val TAG = "DynamicShortcutActivity"
//    }

    val TAG = DynamicShortcutActivity :: class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_shortcurt)

        var asc = Array(5, {i -> (i * i).toString()})
        for(ch in asc) {
            Log.d(TAG, ch + " ")
        }
    }
}
