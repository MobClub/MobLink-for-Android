package com.mob.moblink.demo.awake;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.mob.moblink.demo.BaseActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.common.ShareHelper;
import com.mob.moblink.demo.restore.presenter.RestorePresenter;
import com.mob.moblink.demo.restore.view.IRestoreView;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.SPHelper;

import java.util.HashMap;

public class AwakeActivity extends BaseActivity implements IRestoreView {

	private String mobID;
	private RestorePresenter restorePresenter;

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_awake;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		findViewById(R.id.moblink_demo_userBtn).setOnClickListener(this);
		rightIv.setVisibility(View.GONE);
		restorePresenter = new RestorePresenter(this);
	}

	@Override
	protected int getTitleId() {
		return R.string.moblink_demo_awake;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_userBtn:
				getMobIDToShare();
				break;
			default:
				super.onClick(v);
				break;
		}

	}

	@Override
	public void onMobIdGot(String mobId) {
		if (mobId != null) {
			mobID = mobId;
		} else {
			Toast.makeText(this, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
		}
		share();
	}

	@Override
	public void onMobIdError(Throwable t) {
		if (t != null) {
			Toast.makeText(this, "error = " + t.getMessage(), Toast.LENGTH_SHORT).show();
		}
		share();
	}

	private void getMobIDToShare() {
		if (!TextUtils.isEmpty(mobID)) {
			share();
			return;
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", SPHelper.getDemoUserId());
		params.put("scene", 2007);
		restorePresenter.getMobId(CommonUtils.WAKEUP_PATH, params);
	}

	private void share() {
		String title = getString(R.string.moblink_demo_app_name);
		String text = getString(R.string.moblink_demo_app_desc);
		String imgPath = CommonUtils.copyImgToSD(this, R.drawable.moblink_demo_icon, "share_link_icon");
		String shareUrl = CommonUtils.getShareUrl() + "/wakeup" + "?id=" + SPHelper.getDemoUserId();
		if (!TextUtils.isEmpty(mobID)) {
			shareUrl += "&mobid=" + mobID;
		}
		ShareHelper.showShareReal(this, title, text, shareUrl, imgPath);
	}
}
