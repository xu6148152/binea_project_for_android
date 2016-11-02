package com.zepp.www.searchfilter.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.zepp.www.searchfilter.R
import kotlinx.android.synthetic.main.view_collapse.view.*


//  Created by xubinggui on 02/11/2016.
//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖镇楼                  BUG辟易 
class CollapseView : FrameLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_collapse, this, true)
    }

    internal fun setText(text: String) {
        buttonOk.text = text
    }

    internal fun setHasText(hasText: Boolean) {
        buttonOk.visibility = if(hasText) View.VISIBLE else GONE
    }

    internal fun rotateArrow(rotation: Float): Unit {
        imageArrow.rotation = rotation
    }

    internal fun turnIntoOkButton(ratio: Float) {
        if(buttonOk.visibility != View.VISIBLE) return
        scale(getDecreasingScale(ratio), getIncreasingScale(ratio))

    }

    private fun scale(okScale: Float, arrowScale: Float) {
        buttonOk.scaleX = okScale
        buttonOk.scaleY = okScale
        imageArrow.scaleX = arrowScale
        imageArrow.scaleY = arrowScale
    }

    private fun getIncreasingScale(ratio: Float): Float = if (ratio < 0.5f) 0f else 2 * ratio - 1

    private fun getDecreasingScale(ratio: Float): Float = if (ratio > 0.5f) 0f else 1 - 2 * ratio

    override fun setOnClickListener(l: OnClickListener?) {
        buttonOk.setOnClickListener(l)
        imageArrow.setOnClickListener(l)
    }

    internal fun turnIntoArrow(ratio: Float) {
        if (buttonOk.visibility != View.VISIBLE) return
        scale(getDecreasingScale(ratio), getIncreasingScale(ratio))
    }
}