package com.zhurui.bunnymall.viewutils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.zhurui.bunnymall.R;
import com.zhurui.bunnymall.home.bean.CustomeChildPropertyBean;
import com.zhurui.bunnymall.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// just test
public class RuleView extends View {

	Paint paint;

	private Context context;

	private int maxValue = 320;
	/**
	 * 起点x的坐标
	 */
	private float startX ;

	private float startY ;
	/**
	 * 刻度线的长度
	 */
	private float yLenght ;
	/**
	 * 刻度的间隙
	 */
	private float gap = 8f;
	/**
	 * 文本的间隙
	 */
	private float textGap = 10f;
	/**
	 * 短竖线的高度
	 */
	private float smallHeight = 38f;
	/**
	 * 长竖线的高度
	 */
	private float largeHeight = 42f;

	/**
	 * 文本显示格式化
	 */
	private DecimalFormat format;

	private DisplayMetrics metrics = null;
	/**
	 * 文本的字体大小
	 */
	private float mFontSize;

	private Handler mScrollHandler = null;

	private MySelfHorizontalScrollView horizontalScrollView;

	private int mCurrentX = -999999999;
	/**
	 * 刻度进制
	 */
	private float unit = 10f;

	boolean isDraw = true;

	List<CustomeChildPropertyBean> chimaList;
	public RuleView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public void setHorizontalScrollView(MySelfHorizontalScrollView horizontalScrollView) {
		this.horizontalScrollView = horizontalScrollView;
		this.horizontalScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:

					break;
				case MotionEvent.ACTION_MOVE:

					mScrollHandler.removeCallbacks(mScrollRunnable);
					break;
				case MotionEvent.ACTION_UP:

					mScrollHandler.post(mScrollRunnable);
					break;
				}
				return false;
			}
		});
	}

	public RuleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		init();
	}

	public void refreshDataView(List<CustomeChildPropertyBean> chimaList){
		this.chimaList =chimaList;
		init();
//		invalidate();
		requestLayout();
	}

	public void init() {
		format = new DecimalFormat("0.0");

		metrics = new DisplayMetrics();
		WindowManager wmg = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wmg.getDefaultDisplay().getMetrics(metrics);

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(getResources().getDimension(R.dimen.text2));
		paint.setColor(Color.parseColor("#999999"));

		//根据是否是pad 手机 设置不同的尺寸
		if(Utils.isPad(context)){
			// 该尺寸 以1280×800 为准，其的分辨率的不知道情况
			gap = 16f;
			textGap = 20f;
			smallHeight = 76f;
			largeHeight = 84f;

			mFontSize = Utils.dip2px(context, 32);
			startY = Utils.dip2px(context, 96f);
			yLenght = Utils.dip2px(context, 20);
			gap = Utils.dip2px(context, 16f);
			startX = 14;
		}else{
			gap = 8f;
			textGap = 10f;
			smallHeight = 38f;
			largeHeight = 42f;

			mFontSize = Utils.dip2px(context, 16);
			startY = Utils.dip2px(context, 48f);
			yLenght = Utils.dip2px(context, 10);
			gap = Utils.dip2px(context, 8f);
			startX = getResources().getDimension(R.dimen.activity_horizontal_margin);
		}

		// + getResources().getDimension(R.dimen.text_h2)/2.0f
		// Util.dip2px(context, 13f) +

		mScrollHandler = new Handler(context.getMainLooper());

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(chimaList != null && chimaList.size() > 0){
			maxValue = (chimaList.size() + 5) *10;
			Log.i("rule",chimaList.size() + "");
		}else{
			maxValue = 30 *10;
		}
		float width =  maxValue * gap - getResources().getDimension(R.dimen.activity_horizontal_margin)*2.0f ;

		// int widthMode = MeasureSpec.getMode(heightMeasureSpec);
		// if(widthMode == MeasureSpec.AT_MOST){
		// Log.d("TAG", "mode::AT_MOST");
		// }else if(widthMode == MeasureSpec.EXACTLY){
		// Log.d("TAG", "mode::EXACTLY");
		// }else if(widthMode == MeasureSpec.UNSPECIFIED){
		// Log.d("TAG", "mode::UNSPECIFIED");
		// }

		setMeasuredDimension((int) width, heightMeasureSpec);
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);

		// 画刻度线
		paint.setColor(Color.parseColor("#dfdfdf"));// 刻度颜色
		for (int i = 0; i < maxValue; i++) {

			if (i % 10 != 0) {
				yLenght = Utils.dip2px(context, largeHeight);//22 //0,20,0,22+20
			} else {
				yLenght = Utils.dip2px(context, smallHeight);//10 //0,20,0,22+10  // 0, 22+10 ,0,20
			}
			//根据是否是pad 手机 设置不同的尺寸
			if(Utils.isPad(context)){
				// 该尺寸 以1280×800 为准，其的分辨率的不知道情况
				canvas.drawLine(i * gap - startX *6 + 50, startY, i * gap - startX *6 + 50, yLenght , paint);
			}else{
				canvas.drawLine(i * gap - startX *6 +4, startY, i * gap - startX *6 + 4, yLenght , paint);
			}
		}
//		canvas.drawLine( 0, startY, maxValue*gap + startX, startY , paint);
		paint.setTextSize(mFontSize);

		// 每10个刻度写一个数字
		textGap = gap * unit;

		// 画刻度文字30
		paint.setColor(Color.parseColor("#dfdfdf"));// 文字颜色
		if(chimaList != null && chimaList.size() > 0){
			for (int i = 0; i < chimaList.size(); i++) {
				String text = chimaList.get(i).getName();
				// 获取文本的宽度
				float width = Utils.px2dip(context, calculateTextWidth(text)) / 2f;
				//根据是否是pad 手机 设置不同的尺寸
				if(Utils.isPad(context)){
					// 该尺寸 以1280×800 为准，其的分辨率的不知道情况
					canvas.drawText(text, startX - width + (i+1) * textGap + gap*6 + 10,
							(Utils.dip2px(context, 140) - Utils.dip2px(context, 80)) , paint);
				}else{
					canvas.drawText(text, startX - width + (i+1) * textGap + gap*4,
							(Utils.dip2px(context, 70) - Utils.dip2px(context, 40)) , paint);
				}
			}
		}
	}

	/**
	 * 获取TextView中文本的宽度
	 * 
	 * @param text
	 * @return
	 */
	private float calculateTextWidth(String text) {
		if (TextUtils.isEmpty(text)) {
			return 0;
		}
		TextPaint textPaint = new TextPaint();
		textPaint.setTextSize(mFontSize * metrics.scaledDensity);
		final float textWidth = textPaint.measureText(text);

		return textWidth;
	}

	DecimalFormat df = new DecimalFormat("0.0");

	/**
	 * 当滑动尺子的时候
	 * 
	 * @param l
	 * @param t
	 * @param oldl
	 * @param oldt
	 */

	int scrollWidth = 0;

	public void setScrollerChanaged(int l, int t, int oldl, int oldt) {
		// 滑动的距离
		scrollWidth = l;

		float number = scrollWidth / gap;
		float result = number / unit;
		int a = (int)Math.rint(result);
		if(a < chimaList.size()){
			String s = chimaList.get(a).getName();
//			listener.onSlide(chimaList.get(a));
		}else{
			String s = chimaList.get(chimaList.size() -1).getName();
//			listener.onSlide(chimaList.get(chimaList.size() -1));
		}
	}

	public onChangedListener listener;

	public interface onChangedListener {

		void onSlide(CustomeChildPropertyBean c);
	}

	public void onChangedListener(onChangedListener listener) {
		this.listener = listener;
	}

	/**
	 * 滚动监听线程
	 */
	private Runnable mScrollRunnable = new Runnable() {

		@Override
		public void run() {
			if (mCurrentX == horizontalScrollView.getScrollX()) {// 滚动停止了
				try {
					float x = horizontalScrollView.getScrollX();
					float value = (x / (gap * unit));// 当前的值
					int a = (int)Math.rint(value);
					String s = a +"";

					// 当滑动的范围超过list的大小，将取list最后一个值显示
					if(chimaList != null && chimaList.size() > 0){
						if(a == chimaList.size() -1 || a >=chimaList.size()){
							int scrollX = (int) (Double.parseDouble(chimaList.size()-1 +"") * gap * unit );
							horizontalScrollView.smoothScrollTo(scrollX, 0);
							listener.onSlide(chimaList.get(chimaList.size() -1));
						}else{
							listener.onSlide(chimaList.get(a));
							int scrollX = (int) (Double.parseDouble(s) * gap * unit );
							horizontalScrollView.smoothScrollTo(scrollX, 0);
						}
					}
				} catch (NumberFormatException numExp) {
					numExp.printStackTrace();
				}
				mScrollHandler.removeCallbacks(this);
			} else {
				mCurrentX = horizontalScrollView.getScrollX();
				mScrollHandler.postDelayed(this, 50);
			}
		}
	};

	/**
	 * 设置默认刻度尺的刻度值,不会滚动到相应的位置
	 * 
	 * @param scaleValue
	 */
	public void setDefaultScaleValue(int scaleValue) {

		final int scrollX = (int) ((scaleValue - 1.0f) * gap * unit);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if(scaleValue > 0 && chimaList != null && chimaList.size() > 0){
					listener.onSlide(chimaList.get(scaleValue-1));
					horizontalScrollView.smoothScrollTo(scrollX, 0);
				}
			}
		}, 100);
	}

	/**
	 * 设置刻度最小值
	 * 
	 * @return
	 */
	public void setMinScaleValue(Float minScaleValue) {
		// this.minScaleValue = minScaleValue;
	}

	/**
	 * 获取刻度最大值
	 * 
	 * @return
	 */
	public Float getMaxScaleValue() {
		// return maxScaleValue;
		return Float.parseFloat(chimaList.size()+"");
	}

	/**
	 * 设置刻度最大值
	 * 
	 * @return
	 */
	public void setMaxScaleValue(Float maxScaleValue) {
		// this.maxScaleValue = maxScaleValue;
	}

	/**
	 * 设置当前刻度尺的刻度值,并滚动到相应的位置
	 * 
	 * @param s
	 */
	public void setScaleScroll(String s) {

//		int scrollX = (int) ((scaleValue - 1.0f) * gap * unit);
		int scrollX = (int) (Double.parseDouble(s) * gap * unit );
		horizontalScrollView.smoothScrollTo(scrollX, 0);
	}
}
