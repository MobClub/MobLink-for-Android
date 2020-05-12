package com.mob.moblink.demo.splash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.mob.moblink.demo.MainActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.util.SPHelper;
import com.mob.moblink.demo.util.statusbar.StatusBarCompat;
import com.mob.tools.gui.MobViewPager;

public class SplashActivity extends Activity implements ISplashView, View.OnClickListener {

	private MobViewPager mobViewPager;
	private SplashViewPagerAdapter splashViewPagerAdapter;
	private int[] indicatorIds = new int[]{R.id.moblink_demo_indicator1, R.id.moblink_demo_indicator2, R.id.moblink_demo_indicator3};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (SPHelper.demoHasFirst()) {
			gotoMainActivity();
		}
		setContentView(R.layout.moblink_demo_activity_splash);
		StatusBarCompat.setStatusBarColor(this, Color.WHITE, true);

		initView();
	}

	private void initView() {
		mobViewPager = (MobViewPager) findViewById(R.id.moblink_demo_splash_mobviewpager);

		findViewById(R.id.moblink_demo_splash_skip).setOnClickListener(this);
		findViewById(indicatorIds[0]).setSelected(true);
		initData();
	}

	private void initData() {
		splashViewPagerAdapter = new SplashViewPagerAdapter(this, this);
		mobViewPager.setAdapter(splashViewPagerAdapter);
	}

	@Override
	public void onScreenChange(int currentScreen, int lastScreen) {
		for (int i = 0;i<indicatorIds.length;i++) {
			if (currentScreen == i) {
				findViewById(indicatorIds[i]).setSelected(true);
			}else {
				findViewById(indicatorIds[i]).setSelected(false);
			}
		}
	}

	@Override
	public void gotoMainActivity() {
		startActivity(new Intent(this, MainActivity.class));
		SPHelper.setDemoFirst(true);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_splash_skip:
				gotoMainActivity();
				break;
		}
	}
}
