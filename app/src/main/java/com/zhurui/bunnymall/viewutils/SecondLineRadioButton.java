package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.zhurui.bunnymall.R;

/**
 * Created by zhoux on 2017/8/14.
 */

public class SecondLineRadioButton extends RadioButton {
    private float textSize;
    private float secondTextSize;
    private int textColor;
    private int selectedColor;
    private String first_text;
    private String second_text;
    private Rect rect = new Rect();
    private Context context;
    public SecondLineRadioButton(Context context) {
        this(context, null);
    }


    public SecondLineRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setWillNotDraw(false);
        initAttrs(attrs);

    }

    public SecondLineRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }


    private void initAttrs(AttributeSet attrs){
        TypedArray tintTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.Secondlinebutton);
        textSize = tintTypedArray.getDimension(R.styleable.Secondlinebutton_first_size, 26);
        secondTextSize = tintTypedArray.getDimension(R.styleable.Secondlinebutton_second_size, 24);
        textColor = tintTypedArray.getColor(R.styleable.Secondlinebutton_text_color, context.getResources().getColor(R.color.gray));
        selectedColor = tintTypedArray.getColor(R.styleable.Secondlinebutton_select_text_color, context.getResources().getColor(R.color.navigation_bar_bg));
        first_text = tintTypedArray.getString(R.styleable.Secondlinebutton_first_text);
        second_text = tintTypedArray.getString(R.styleable.Secondlinebutton_second_text);
        tintTypedArray.recycle();
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制第一行文字
        if (!isChecked()) {
            Paint paint = new Paint();
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            paint.getTextBounds(first_text, 0, first_text.length(), rect);
            float tagWidth =rect.width();
            int tagHeight = rect.height();
            int x = (int) (this.getWidth() - tagWidth) / 2;
            int y = this.getHeight() / 2;
            canvas.drawText(first_text, x, y, paint);
            //绘制第二行文字
            Paint paint1 = new Paint();
            paint1.setTextSize(secondTextSize);
            paint1.setColor(textColor);
            float numWidth = paint.measureText(second_text);
            int x1 = (int) (this.getWidth() - numWidth) / 2;
            int y1 = this.getHeight()/2 + tagHeight + 10;
            canvas.drawText(second_text, x1, y1, paint1);
        } else {
            Paint paint = new Paint();
            paint.setTextSize(textSize);
            paint.setColor(selectedColor);
            paint.getTextBounds(first_text, 0, first_text.length(), rect);
            float tagWidth = paint.measureText(first_text);
            int tagHeight = rect.height();
            int x = (int) (this.getWidth() - tagWidth) / 2;
            int y = (this.getHeight()-15) / 2;
            canvas.drawText(first_text, x, y, paint);
            //绘制第二行文字
            Paint paint1 = new Paint();
            paint1.setTextSize(secondTextSize);
            paint1.setColor(selectedColor);
            float numWidth = paint.measureText(second_text);
            int x1 = (int) (this.getWidth() - numWidth) / 2;
            int y1 = (this.getHeight()-15)/ 2 + tagHeight + 10;
            canvas.drawText(second_text, x1, y1, paint1);
        }

    }

    public void setFirst_text(String str) {
        this.first_text = str;
    }

    public void setSecond_text(String str) {
        this.second_text = str;
    }

    public void setFirst_size(float first_size) {
        this.textSize = first_size;
    }

    public void setSecondTextSize(float secondTextSize) {
        this.secondTextSize = secondTextSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setSelectedColor(int textColor) {
        this.selectedColor = textColor;
    }

}
