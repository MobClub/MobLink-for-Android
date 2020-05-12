package com.mob.moblink.demo.analyze;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.mob.moblink.demo.BaseActivity;
import com.mob.moblink.demo.R;

public class AnalyzeActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_analy;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		findViewById(R.id.moblink_demo_localRL).setOnClickListener(this);
		findViewById(R.id.moblink_demo_shareRL).setOnClickListener(this);
		findViewById(R.id.moblink_demo_restoreRL).setOnClickListener(this);
	}

	@Override
	protected int getTitleId() {
		return R.string.moblink_demo_analy;
	}

	@Override
	protected int getTitleBarColor() {
		return Color.WHITE;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_localRL:
				gotoAnalyzeDetailPage(AnalyzeDetailActivity.LOCAL_INVITE_ANALYZE_TYPE);
				break;
			case R.id.moblink_demo_shareRL:
				gotoAnalyzeDetailPage(AnalyzeDetailActivity.SHARE_INVITE_ANALYZE_TYPE);
				break;
			case R.id.moblink_demo_restoreRL:
				gotoAnalyzeDetailPage(AnalyzeDetailActivity.RESTORE_ANALYZE_TYPE);
				break;
			default:
				super.onClick(v);
				break;
		}
	}

	private void gotoAnalyzeDetailPage(int type) {
		Intent intent = new Intent(this, AnalyzeDetailActivity.class);
		intent.putExtra("analyzeType", type);
		startActivity(intent);
	}
}
