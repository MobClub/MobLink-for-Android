package com.mob.moblink.demo.match;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.demo.R;
import com.mob.moblink.demo.ShareableActivity;
import com.mob.moblink.demo.common.ShareHelper;
import com.mob.moblink.demo.profile.FriendActivity;
import com.mob.moblink.demo.restore.presenter.RestorePresenter;
import com.mob.moblink.demo.restore.view.IRestoreView;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.SPHelper;

import java.util.HashMap;

public class MatchActivity extends ShareableActivity implements IRestoreView {

	private TextView stepOneTv;
	private TextView stepThreeTv;
	private String mobID;
	private RestorePresenter restorePresenter;

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_match;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		restorePresenter = new RestorePresenter(this);
		rightIv.setVisibility(View.VISIBLE);
		stepOneTv = (TextView) findViewById(R.id.moblink_demo_step_oneTv);
		stepThreeTv = (TextView) findViewById(R.id.moblink_demo_step_threeTv);

		setTextAndImageToStepOne(R.drawable.moblink_demo_match_share);
		stepThreeTv.setText(Html.fromHtml(getString(R.string.moblink_demo_step_three)));
		stepThreeTv.setOnClickListener(this);
	}

	@Override
	protected int getTitleId() {
		return R.string.moblink_demo_relation_match;
	}

	private void setTextAndImageToStepOne(int dra) {
		Drawable drawable = getResources().getDrawable(dra);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

		SpannableString spannableString = new SpannableString("pics");

		ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);

		spannableString.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

		stepOneTv.append(getString(R.string.moblink_demo_step_one1));
		stepOneTv.append(spannableString);
		stepOneTv.append(getString(R.string.moblink_demo_step_one2));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_step_threeTv:
				startActivity(new Intent(this, FriendActivity.class));
				break;
			default:
				super.onClick(v);
				break;
		}
	}

	@Override
	protected void onRightEvent() {
		getMobIDToShare();
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
		params.put("scene", CommonUtils.SCENE_MATCH);
		restorePresenter.getMobId(CommonUtils.MATCH_PATH, params);
	}

	private void share() {
		String imgPath = CommonUtils.copyImgToSD(this, R.drawable.moblink_demo_match_share_icon, "match_icon");
		String title = getString(R.string.moblink_demo_match_share_tilte);
		String text = getString(R.string.moblink_demo_match_share_text);
		String shareUrl = CommonUtils.getShareUrl() + CommonUtils.MATCH_PATH + "?id=" + SPHelper.getDemoUserId();
		if (!TextUtils.isEmpty(mobID)) {
			shareUrl += "&mobid=" + mobID;
		}
		ShareHelper.showShareReal(this, title, text, shareUrl, imgPath);
	}
}
