package com.mob.moblink.demo.homepage;

import android.content.Intent;
import android.view.View;

import com.mob.moblink.demo.MainActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.analyze.AnalyzeActivity;
import com.mob.moblink.demo.awake.AwakeActivity;
import com.mob.moblink.demo.invite.InviteActivity;
import com.mob.moblink.demo.match.MatchActivity;
import com.mob.moblink.demo.restore.RestoreActivity;

public class HomePage implements View.OnClickListener {

	private MainActivity mainActivity;
	private View view;

	public HomePage(View view, MainActivity activity) {
		this.view = view;
		this.mainActivity = activity;

		initView();
	}

	private void initView() {
		view.findViewById(R.id.moblink_demo_layout_awake).setOnClickListener(this);
		view.findViewById(R.id.moblink_demo_layout_restore).setOnClickListener(this);
		view.findViewById(R.id.moblink_demo_layout_invite).setOnClickListener(this);
		view.findViewById(R.id.moblink_demo_layout_relation_match).setOnClickListener(this);
		view.findViewById(R.id.moblink_demo_layout_analy).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_layout_awake:
				gotoActivity(AwakeActivity.class);
				break;
			case R.id.moblink_demo_layout_restore:
				gotoActivity(RestoreActivity.class);
				break;
			case R.id.moblink_demo_layout_invite:
				gotoActivity(InviteActivity.class);
				break;
			case R.id.moblink_demo_layout_relation_match:
				gotoActivity(MatchActivity.class);
				break;
			case R.id.moblink_demo_layout_analy:
				gotoActivity(AnalyzeActivity.class);
				break;
		}
	}

	private void gotoActivity(Class clazz) {
		mainActivity.startActivity(new Intent(mainActivity, clazz));
	}
}
