package com.mob.moblink.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.moblink.demo.util.statusbar.StatusBarCompat;

/**
 * Activity的基类, 做一些公共的处理.
 */

public abstract class BaseActivity extends Activity implements View.OnClickListener {
	protected ImageView leftIv;
	protected TextView centerTv;
	protected ImageView rightIv;
	private View titleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 加载View
		LayoutInflater inflater = LayoutInflater.from(this);
		ViewGroup container = (ViewGroup) inflater.inflate(R.layout.moblink_demo_container, null);
		int contentId = getContentViewId();
		if (contentId > 0) {
			View content = inflater.inflate(contentId, null);
			container.addView(content, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		}
		setContentView(container);
		initView();
		// 通知子类View加载完毕
		onViewCreated(savedInstanceState);

		if (getTitleId() > 0) {
			centerTv.setText(getTitleId());
		}
		if (getTitleBarColor() == Color.WHITE) {
			StatusBarCompat.setStatusBarColor(this, getTitleBarColor(), true);
			setTitleBar(true);
		} else {
			StatusBarCompat.setStatusBarColor(this, getTitleBarColor(), false);
			setTitleBar(false);
		}
	}

	protected abstract int getContentViewId();
	protected abstract void onViewCreated(Bundle savedInstanceState);
	protected int getTitleId(){return 0;}
	protected void onRightEvent(){}
	protected int getTitleBarColor() {
		return 0xFF3266FF;
	}
	/**
	 * 当左上角返回按钮点击时调用
	 * @return true 事情被消耗
	 */
	protected boolean onLeftEvent() {
		return false;
	}

	protected void setTitleBar(boolean white) {
		if (white) {
			titleView.setBackgroundColor(Color.WHITE);
			centerTv.setTextColor(getResources().getColor(R.color.moblink_demo_text));
			leftIv.setImageResource(R.drawable.moblink_demo_back);
			rightIv.setImageResource(R.drawable.moblink_demo_share);
			rightIv.setVisibility(View.INVISIBLE);
		}
	}

	private void initView() {
		titleView = findViewById(R.id.moblink_demo_title_bar);
		leftIv = (ImageView) findViewById(R.id.molink_demo_title_bar_left);
		leftIv.setOnClickListener(this);
		centerTv = (TextView) findViewById(R.id.moblink_demo_title_bar_center);
		rightIv = (ImageView) findViewById(R.id.molink_demo_title_bar_right);
		rightIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.molink_demo_title_bar_left: {
				// 通知子类左上角View点击事件
				boolean handled = onLeftEvent();
				if (!handled) {
					finish();
					MainActivity.launcherMainIfNecessary(BaseActivity.this);
				}
				break;
			}
			case R.id.molink_demo_title_bar_right: {
				// 通知子类右上角View点击事件
				onRightEvent();
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MainActivity.launcherMainIfNecessary(this);
	}
}
