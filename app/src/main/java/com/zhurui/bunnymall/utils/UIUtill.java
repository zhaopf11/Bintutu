package com.zhurui.bunnymall.utils;

import android.content.Context;

/**
 * Created by zhaopf on 2017/9/25 0025.
 */

public class UIUtill {
    public static int dp2px(Context context, int dpValue) {
        return (int) context.getResources().getDisplayMetrics().density * dpValue;
    }
}
