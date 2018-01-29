package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import dagger.Provides;

/**
 * Created by zhoux on 2017/7/27.
 */

public class ArrowToRight extends View{

    private int leftTextColor;
    private int rightTextColor;
    private int leftBgColor;
    private int rigtBgColor;
    private int arrowColor;
    private int textSize;
    private int DEFAULT_TEXT_COLOR= Color.BLACK;
    private int DEFAULT_BG_COLOR=Color.WHITE;
    private int DEFAULT_ARROW_COLOR=Color.GRAY;

    public ArrowToRight(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


    }




}
