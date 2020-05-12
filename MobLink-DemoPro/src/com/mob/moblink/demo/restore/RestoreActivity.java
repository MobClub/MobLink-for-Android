package com.mob.moblink.demo.restore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.mob.moblink.demo.BaseActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.restore.game.GameActivity;
import com.mob.moblink.demo.restore.novel.NovelListActivity;
import com.mob.moblink.demo.restore.view.NewsActivity;

public class RestoreActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_restore;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		findViewById(R.id.moblink_demo_newsRl).setOnClickListener(this);
		findViewById(R.id.moblink_demo_novelRl).setOnClickListener(this);
		findViewById(R.id.moblink_demo_gamelRl).setOnClickListener(this);
	}

	@Override
	protected int getTitleBarColor() {
		return Color.WHITE;
	}

	@Override
	protected int getTitleId() {
		return R.string.moblink_demo_restore;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_newsRl:
				startActivity(new Intent(this, NewsActivity.class));
				break;
			case R.id.moblink_demo_novelRl:
				startActivity(new Intent(this, NovelListActivity.class));
				break;
			case R.id.moblink_demo_gamelRl:
				startActivity(new Intent(this, GameActivity.class));
				break;
			default:
				super.onClick(v);
				break;
		}
	}
}
