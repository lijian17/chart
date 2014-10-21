package net.dxs.chart;

import net.dxs.chart.utils.AppUtils;
import net.dxs.chart.utils.DensityUtil;
import net.dxs.chart.view.ChartView;
import net.dxs.chart.view.ChartViewGroup;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;

public class MainActivity extends Activity {

	private String[] mArr_periodName = { "期次" };
	private String[] mArr_periodTitle = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };
	private String[] mArr_Num = new String[1000];
	private String[] mArr_period = new String[50];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		initData();
		initUI();
	}

	private void initUI() {
		for (int i = 0; i < mArr_Num.length; i++) {
			mArr_Num[i] = String.valueOf(i);
		}
		for (int i = 0; i < mArr_period.length; i++) {
			mArr_period[i] = String.valueOf(201401 + i);
		}

		ChartView mPeriodName_chartView = new ChartView(this, DensityUtil.dip2px(this, 80), DensityUtil.dip2px(this, 40), 1, mArr_periodName);
		ChartView mPeriodTitle_chartView = new ChartView(this, DensityUtil.dip2px(this, 40), DensityUtil.dip2px(this, 40), 20, mArr_periodTitle);
		ChartView mPeriod_chartView = new ChartView(this, DensityUtil.dip2px(this, 80), DensityUtil.dip2px(this, 40), 1, mArr_period);
		ChartView mNum_chartView = new ChartView(this, DensityUtil.dip2px(this, 40), DensityUtil.dip2px(this, 40), 20, mArr_Num);

		ChartViewGroup chartViewGroup = new ChartViewGroup(this);

		chartViewGroup.addView(mPeriodName_chartView);
		chartViewGroup.addView(mPeriodTitle_chartView);
		chartViewGroup.addView(mPeriod_chartView);
		chartViewGroup.addView(mNum_chartView);

		setContentView(chartViewGroup);

	}

	private void initData() {
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		AppUtils.DENSITY = displayMetrics.density;
		AppUtils.WIDTH = displayMetrics.widthPixels;
		AppUtils.HEIGHT = displayMetrics.heightPixels;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
