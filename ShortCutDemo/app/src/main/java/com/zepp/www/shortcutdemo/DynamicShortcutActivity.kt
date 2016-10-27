package com.zepp.www.shortcutdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class DynamicShortcutActivity : AppCompatActivity() {

    object ACTION {
//        @JvmField val ACTION = "com.zepp.www.OPEN_DYNAMIC_SHORTCUT"
        const val ACTION = "com.zepp.www.OPEN_DYNAMIC_SHORTCUT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_shortcurt)
    }
}
