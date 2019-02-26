package com.mob.moblink.demo.invite;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.demo.R;
import com.mob.moblink.demo.ShareableActivity;
import com.mob.moblink.demo.model.DemoUser;
import com.mob.moblink.demo.model.PushInfo;
import com.mob.moblink.demo.restore.presenter.RestorePresenter;
import com.mob.moblink.demo.restore.view.IRestoreView;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.DemoAsyncListener;
import com.mob.moblink.demo.util.DemoAsyncProtocol;
import com.mob.moblink.demo.util.QRcodeUtils;
import com.mob.moblink.demo.util.SPHelper;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;

public class LocalInviteActivity extends ShareableActivity implements IRestoreView {

	private ImageView ivQrcode;
	private AsyncImageView userIcon;
	private TextView userNameTv;
	private TextView getMoneyTv;
	private TextView numTv;
	private String mobID;
	private RestorePresenter restorePresenter;

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_local_invite;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		ivQrcode = (ImageView) findViewById(R.id.moblink_demo_local_qrcodeIv);
		userIcon = (AsyncImageView) findViewById(R.id.moblink_demo_userIv);
		userNameTv = (TextView) findViewById(R.id.moblink_demo_usernameTv);
		getMoneyTv = (TextView) findViewById(R.id.moblink_demo_getMoneyTv);
		numTv = (TextView) findViewById(R.id.moblink_demo_numTv);
		restorePresenter = new RestorePresenter(this);

		userIcon.setRound(ResHelper.dipToPx(this, 360));
		getUserInfo();
		getMobIDToCreateQRCode();
	}

	private void getUserInfo() {
		DemoAsyncProtocol.getUserInfo(new DemoAsyncListener<DemoUser>() {
			@Override
			public void onSuccess(DemoUser demoUser) {
				if (null != demoUser && null != demoUser.getRes()) {
					userIcon.execute(demoUser.getRes().avatar, R.drawable.moblink_demo_default_user);
					userNameTv.setText(demoUser.getRes().nickname);
					getPushInfo();
				}
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {
				Toast.makeText(LocalInviteActivity.this, "responseCode : " + responseCode + " error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getPushInfo() {
		DemoAsyncProtocol.queryPushInfo(new DemoAsyncListener<PushInfo>() {
			@Override
			public void onSuccess(PushInfo pushInfo) {
				if (null != pushInfo && null != pushInfo.getRes()) {
					getMoneyTv.setText(pushInfo.getRes().getMoney + "");
					numTv.setText(pushInfo.getRes().num + "");
				}
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {
				Toast.makeText(LocalInviteActivity.this, "responseCode : " + responseCode + " error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void createQRCode() {
		Bitmap bitmap = null;
		try {
			String shareUrl = CommonUtils.getShareUrl() + CommonUtils.LOCAL_INVITE_PATH + "?id=" + SPHelper.getDemoUserId();
			if (!TextUtils.isEmpty(mobID)) {
				shareUrl += "&mobid=" + mobID;
			}
			bitmap = QRcodeUtils.encodeAsBitmap(shareUrl, 800, 800);
			ivQrcode.setImageBitmap(bitmap);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (bitmap == null) {
			Toast.makeText(getApplicationContext(), R.string.txt_moblink_share_qrcode_failed_1, Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@Override
	protected int getTitleId() {
		return R.string.moblink_demo_local_invite;
	}

	@Override
	protected int getTitleBarColor() {
		return Color.WHITE;
	}

	@Override
	public void onMobIdGot(String mobId) {
		if (mobId != null) {
			mobID = mobId;
		} else {
			Toast.makeText(this, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
		}
		createQRCode();
	}

	@Override
	public void onMobIdError(Throwable t) {
		if (t != null) {
			Toast.makeText(this, "error = " + t.getMessage(), Toast.LENGTH_SHORT).show();
		}
		createQRCode();
	}

	private void getMobIDToCreateQRCode() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", SPHelper.getDemoUserId());
		params.put("scene", CommonUtils.SCENE_LOCAL_INVITE);
		restorePresenter.getMobId(CommonUtils.LOCAL_INVITE_PATH, params);
	}
}
