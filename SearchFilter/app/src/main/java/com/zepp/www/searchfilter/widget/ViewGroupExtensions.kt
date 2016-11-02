package com.zepp.www.searchfilter.widget

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup


//  Created by xubinggui on 01/11/2016.
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

internal fun calculateSize(measureSpec: Int, desciredSize: Int): Int {
    val mode = View.MeasureSpec.getMode(measureSpec)
    val size = View.MeasureSpec.getSize(measureSpec)

    val actualSize = when(mode) {
        View.MeasureSpec.EXACTLY -> size
        View.MeasureSpec.AT_MOST -> Math.min(desciredSize, size)
        else -> desciredSize
    }
    return actualSize
}

internal fun ViewGroup.dpToPx(dp: Float): Int {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics).toInt()
}

internal fun ViewGroup.dpToPx(dp: Int): Int {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics).toInt()
}

internal fun ViewGroup.getDimen(res: Int): Int = context.resources.getDimensionPixelOffset(res)

internal fun calculateX(position: Int, size: Int, minMargin: Int, itemSize: Int): Int {
    val realMargin = calculateMargin(size, itemSize, minMargin)
    return position * itemSize + position * realMargin + realMargin
}

internal fun calculateMargin(width: Int, itemWidth: Int, margin: Int): Int {
    val count = calculateCount(width, itemWidth, margin)
    return if (count > 0) (width - count * itemWidth) / count else 0
}

internal fun calculateCount(width: Int, itemWidth: Int, margin: Int): Int = width / (itemWidth + margin)

internal fun isClick(startX: Float, startY: Float, x: Float, y: Float): Boolean = Math.abs(x - startX) < 20 && Math.abs(y - startY) < 20