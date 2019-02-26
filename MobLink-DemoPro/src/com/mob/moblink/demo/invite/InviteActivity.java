package com.mob.moblink.demo.invite;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.mob.moblink.demo.BaseActivity;
import com.mob.moblink.demo.R;

public class InviteActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_invite;
	}

	@Override
	protected int getTitleId() {
		return R.string.moblink_demo_invite;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		findViewById(R.id.moblink_demo_invite_localRl).setOnClickListener(this);
		findViewById(R.id.moblink_demo_invite_shareRl).setOnClickListener(this);
	}

	@Override
	protected int getTitleBarColor() {
		return Color.WHITE;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_invite_localRl:
				startActivity(new Intent(this, LocalInviteActivity.class));
				break;
			case R.id.moblink_demo_invite_shareRl:
				startActivity(new Intent(this, ShareInviteActivity.class));
				break;
			default:
				super.onClick(v);
				break;
		}
	}
}
