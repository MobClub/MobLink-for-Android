package com.mob.moblink.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mob.moblink.demo.util.CommonUtils;

/**
 * Activity的基类, 做一些公共的处理.
 */

public class BaseActivity extends Activity implements View.OnClickListener {

	private boolean isInitView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (!isInitView) {
			initView();
			isInitView = true;
		}
	}

	private void initView() {
		View v = findViewById(R.id.iv_back);
		if (null != v) {
			v.setOnClickListener(this);
		}
	}

	/**
	 * 当左上角返回按钮点击时调用
	 * @return true 事情被消耗
	 */
	protected boolean onBackImagePressed() {
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back: {
				boolean handled = onBackImagePressed();
				if (!handled) {
					finish();
					MainActivity.launcherMainIfNecessary(BaseActivity.this);
				}
			} break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MainActivity.launcherMainIfNecessary(this);
	}
}
