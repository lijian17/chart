package net.dxs.chart.view;

import net.dxs.chart.utils.AppUtils;
import net.dxs.chart.utils.DensityUtil;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 走势图组件组
 * 
 * @author lijian-pc
 * 
 */
public class ChartViewGroup extends ViewGroup {

	protected static final String TAG = "ChartViewGroup";
	private Context mContext;
	/** 手势识别器 **/
	private GestureDetector mGestureDetector;
	/** 期号名称 **/
	private View mPeriodName_view;
	/** 期号标题 **/
	private View mPeriodTitle_view;
	/** 期号 **/
	private View mPeriod_view;
	/** 号码 **/
	private View mNum_view;

	int tempX = 0;
	int tempY = 0;
	private float mVisibleWidth;
	private float mVisibleHeight;

	public ChartViewGroup(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	private void init() {
		mGestureDetector = new GestureDetector(mContext, new OnGestureListener() {

			private MotionEvent lastE2;

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {

			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				/**
				 * e1 初次触控地图的event1
				 * 
				 * e2 每次触发onScroll函数得到的的event2
				 * 
				 * distance是上一次的event2 减去 当前event2得到的结果 //注意到顺序
				 * 
				 * lastEvent2 - event2 = distance <br>
				 * 
				 * if (lastE2 != null){ Log.i(TAG, "distanceX:" + distanceX +
				 * " = " + (lastE2.getX()-e2.getX())); Log.i(TAG, "distanceY:" +
				 * distanceY + " = " + (lastE2.getY()-e2.getY())); } lastE2 =
				 * MotionEvent.obtain(e2);
				 */
				if (lastE2 != null) {
					Log.i(TAG, "distanceX:" + distanceX + " = " + (lastE2.getX() - e2.getX()));
					Log.i(TAG, "distanceY:" + distanceY + " = " + (lastE2.getY() - e2.getY()));
				}
				lastE2 = MotionEvent.obtain(e2);

//				tempX += distanceX;
//				tempY += distanceY;
				tempX += Math.ceil(distanceX);
				tempY += Math.ceil(distanceY);

				int width = (int) (mNum_view.getMeasuredWidth() - mVisibleWidth);
				int height = (int) (mNum_view.getMeasuredHeight() - mVisibleHeight);

				if (tempX < 0) {
					tempX = 0;
				}
				if (tempY < 0) {
					tempY = 0;
				}
				if (tempX > width) {
					tempX = width;
				}
				if (tempY > height) {
					tempY = height;
				}
				if (tempX > 0 && tempX < width) {
					System.out.println("左右移动");
					System.out.println("distanceX" + distanceX);
					mNum_view.scrollBy((int) Math.ceil(distanceX), 0);
					mPeriodTitle_view.scrollBy((int) Math.ceil(distanceX), 0);
				}
				if (tempY > 0 && tempY < height) {
					System.out.println("上下移动");
					System.out.println("distanceY" + distanceY);
					mNum_view.scrollBy(0, (int) Math.ceil(distanceY));
					mPeriod_view.scrollBy(0, (int) Math.ceil(distanceY));
				}

				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {

			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				System.out.println("onFling");

				return false;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measureWidth = measureWidth(widthMeasureSpec);
		int measureHeight = measureHeight(heightMeasureSpec);

		// 计算自定义的ViewGroup中所有子控件的大小
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		// 设置自定义的控件MyViewGroup的大小
		setMeasuredDimension(measureWidth, measureHeight);
	}

	private int measureWidth(int pWidthMeasureSpec) {
		int result = 0;
		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

		switch (widthMode) {
		/**
		 * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
		 * MeasureSpec.AT_MOST。
		 * 
		 * 
		 * MeasureSpec.EXACTLY是精确尺寸，
		 * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
		 * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
		 * 
		 * 
		 * MeasureSpec.AT_MOST是最大尺寸，
		 * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
		 * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
		 * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
		 * 
		 * 
		 * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
		 * 通过measure方法传入的模式。
		 */
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = widthSize;
			break;
		}
		return result;
	}

	private int measureHeight(int pHeightMeasureSpec) {
		int result = 0;

		int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

		switch (heightMode) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = heightSize;
			break;
		}
		return result;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		mPeriodName_view = getChildAt(0);
		// 获取在onMeasure中计算的视图尺寸
		int mPName_mWidth = mPeriodName_view.getMeasuredWidth();
		int mPName_mHeight = mPeriodName_view.getMeasuredHeight();
		mPeriodName_view.layout(0, 0, mPName_mWidth, mPName_mHeight);

		mPeriodTitle_view = getChildAt(1);
		// 获取在onMeasure中计算的视图尺寸
		int mPTitle_mWidth = mPeriodTitle_view.getMeasuredWidth();
		int mPTitle_mHeight = mPeriodTitle_view.getMeasuredHeight();
		mPeriodTitle_view.layout(mPName_mWidth, 0, mPName_mWidth + mPTitle_mWidth, mPTitle_mHeight);

		mPeriod_view = getChildAt(2);
		// 获取在onMeasure中计算的视图尺寸
		int mP_mWidth = mPeriod_view.getMeasuredWidth();
		int mP_mHeight = mPeriod_view.getMeasuredHeight();
		mPeriod_view.layout(0, mPName_mHeight, mP_mWidth, mPName_mHeight + mP_mHeight);

		mNum_view = getChildAt(3);
		// 获取在onMeasure中计算的视图尺寸
		int mNum_mWidth = mNum_view.getMeasuredWidth();
		int mNum_mHeight = mNum_view.getMeasuredHeight();
		mNum_view.layout(mP_mWidth, mPTitle_mHeight, mP_mWidth + mNum_mWidth, mPTitle_mHeight + mNum_mHeight);

		mVisibleWidth = AppUtils.WIDTH - mPName_mWidth;
		mVisibleHeight = AppUtils.HEIGHT - mPName_mHeight;

	}

	// OnGestureListener mGestureListener = ;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return true;
	}

}
