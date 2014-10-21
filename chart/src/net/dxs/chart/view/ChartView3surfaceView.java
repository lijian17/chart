package net.dxs.chart.view;

import net.dxs.chart.utils.DensityUtil;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class ChartView3surfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private Context mContext;
	private float width;
	private float height;
	private Paint mPaint;
	private RectF mRectF;
	private String[] mStrs;
	private Canvas mCanvas;
	private int mRowNum;

	private SurfaceHolder holder;
	protected boolean isRender;// 线程的开关
	private RenderThread thread;

	public ChartView3surfaceView(Context context, float width, float height, int rowNum, String[] strs) {
		super(context);
		this.mContext = context;
		this.width = width;
		this.height = height;
		this.mRowNum = rowNum;
		this.mStrs = strs;
		holder = this.getHolder();
		holder.addCallback(this);// 保证下面的三个方法有效
		init();
	}

	private void init() {
		LayoutParams params = new LinearLayout.LayoutParams((int) width * mRowNum, (int) height * (mStrs.length / mRowNum));
		// LayoutParams params = new LinearLayout.LayoutParams((int) width,
		// (int) height);
		setLayoutParams(params);
		mPaint = new Paint();
		mRectF = new RectF();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new RenderThread();
		isRender = true;
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isRender = false;
	}

	private class RenderThread extends Thread {
		@Override
		public void run() {
			super.run();
			draw();
		}
	}

	public void draw() {
		// 1 Canvas lockCanvas
		mCanvas = holder.lockCanvas();

		// 2 拿到Canvas可以绘制界面
		drawPane();

		// 3 unLockCanvas(Canvas canvas);解锁画布 post() or postAll() is called
		// 二合一 unlockCanvasAndPost(Canvas canvas);
		holder.unlockCanvasAndPost(mCanvas);
	}

	private void drawPane() {
		for (int i = 0; i < mStrs.length; i++) {
			int x = i % mRowNum;
			int y = i / mRowNum;
			onDrawBackground(i, x, y);
			onDrawBorder(x, y);
			onDrawText(mStrs[i], x, y);
		}
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// System.out.println("onDraw------------->");
	// this.mCanvas = canvas;
	// for (int i = 0; i < mStrs.length; i++) {
	// int x = i % mRowNum;
	// int y = i / mRowNum;
	// onDrawBackground(i, x, y);
	// onDrawBorder(x, y);
	// onDrawText(mStrs[i], x, y);
	// }
	// }

	/**
	 * 绘制背景
	 */
	private void onDrawBackground(int i, int x, int y) {
		mRectF.set(width * x, height * y, width * (x + 1), height * (y + 1));
		mPaint.setShader(null);
		String color;
		if (mRowNum % 2 == 0) {// 如果每行个数是偶数
			if (y % 2 == 0) {// 如果是第偶数行
				if (i % 2 == 0) {
					color = "#ccc0b3";
				} else {
					color = "#eee4da";
				}
			} else {
				if (i % 2 == 0) {
					color = "#eee4da";
				} else {
					color = "#ccc0b3";
				}
			}
		} else {
			if (i % 2 == 0) {
				color = "#eee4da";
			} else {
				color = "#ccc0b3";
			}
		}

		mPaint.setColor(Color.parseColor(color));
		mPaint.setStyle(Style.FILL);
		mCanvas.drawRect(mRectF, mPaint);
	}

	/**
	 * 绘制文本
	 */
	private void onDrawText(String str, int x, int y) {
		mPaint.setShader(null);
		mPaint.setAntiAlias(true);// 去锯齿
		mPaint.setColor(Color.parseColor("#000000"));
		mPaint.setTextSize(DensityUtil.dip2px(mContext, 16));
		mPaint.setTextAlign(Align.CENTER);
		float _x = width * x + width / 2;
		float _y = height * y + height / 2 + mPaint.getFontMetrics().bottom;
		mCanvas.drawText(str, _x, _y, mPaint);
	}

	/**
	 * 绘制边框
	 */
	private void onDrawBorder(int x, int y) {
		mPaint.setShader(null);
		mPaint.setColor(Color.parseColor("#eb8739"));
		mPaint.setStyle(Style.STROKE);

		Path path = new Path();
		path.addRect(width * x, height * y, width * (x + 1), height * (y + 1), Direction.CW);
		mCanvas.drawPath(path, mPaint);
		path.close();
	}

}
