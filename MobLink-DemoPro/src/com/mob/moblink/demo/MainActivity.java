package com.mob.moblink.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mob.MobSDK;
import com.mob.moblink.MobLink;
import com.mob.moblink.demo.homepage.HomePage;
import com.mob.moblink.demo.profile.MinePage;
import com.mob.moblink.demo.util.PrivacyDialog;
import com.mob.moblink.demo.util.statusbar.StatusBarCompat;

public class MainActivity extends Activity implements View.OnClickListener {

	private static Activity sFirstInstance;
	private MinePage minePage;

	private static final int[] TAB_INDEX = new int[]{
			R.id.ll_main,
			R.id.ll_my,
	};
	private static final int[] TAB_CONTENT = new int[]{
			R.id.layout_main,
			R.id.layout_my,
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!MobSDK.isMob()) {
			Intent intent = new Intent();
			intent.setClass(this, PrivacyDialog.class);
			startActivity(intent);
		}

		StatusBarCompat.setStatusBarColor(this, Color.WHITE, true);
		if (null == sFirstInstance) {
			sFirstInstance = this;
			initView();
		} else if (this != sFirstInstance) {
			// 防止微信跳转过来，多个MainActivity界面(是singletop)
//			finish();
		} else {
			initView();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getIntent() != null) {
			int tag = getIntent().getIntExtra("tag", -1);
			if (tag >= 0 && tag < TAB_INDEX.length) {
				switchTab(TAB_INDEX[tag]);
			}
		}
		if (null != minePage) {
			minePage.onResume();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_main:
			case R.id.ll_my: {
				switchTab(v.getId());
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (sFirstInstance == this) {
			sFirstInstance = null;
		}
	}

	private void initView() {
		for (int i = 0; i < TAB_INDEX.length; i++) {
			View v = findViewById(TAB_INDEX[i]);
			v.setOnClickListener(this);
		}
		TextView versionTv = (TextView) findViewById(R.id.moblink_demo_versionTv);
		versionTv.setText(getString(R.string.moblink_demo_version, getVersion()));

		new HomePage(findViewById(R.id.layout_main), this);
		minePage = new MinePage(findViewById(R.id.layout_my), this);

		switchTab(R.id.ll_main);
	}

	/**
	 * 回退时, 必须时拉起launcher.
	 */
	protected static void launcherMainIfNecessary(Activity current) {
		if (null == sFirstInstance) {
			Intent intent = new Intent(current, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			current.startActivity(intent);
		}
	}

	private void switchTab(int tagId) {
		for (int i = 0; i < TAB_INDEX.length; i++) {
			View tab = findViewById(TAB_INDEX[i]);
			View content = findViewById(TAB_CONTENT[i]);
			if (TAB_INDEX[i] == tagId) {
				tab.setSelected(true);
				content.setVisibility(View.VISIBLE);
			} else {
				tab.setSelected(false);
				content.setVisibility(View.INVISIBLE);
			}
		}
	}

	public String getVersion() {
		int version = MobLink.getSdkVersion();
		StringBuilder stringBuilder = new StringBuilder();
		while (version > 10) {
			stringBuilder.insert(0, (version % 100));
			stringBuilder.insert(0, ".");
			version = version / 100;
		}
		stringBuilder.insert(0, version);
		return stringBuilder.toString();
	}

}
