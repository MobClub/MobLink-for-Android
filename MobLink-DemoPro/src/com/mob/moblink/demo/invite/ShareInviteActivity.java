package com.mob.moblink.demo.invite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.demo.R;
import com.mob.moblink.demo.ShareableActivity;
import com.mob.moblink.demo.common.ShareHelper;
import com.mob.moblink.demo.model.DemoUser;
import com.mob.moblink.demo.restore.presenter.RestorePresenter;
import com.mob.moblink.demo.restore.view.IRestoreView;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.DemoAsyncListener;
import com.mob.moblink.demo.util.DemoAsyncProtocol;
import com.mob.moblink.demo.util.QRcodeUtils;
import com.mob.moblink.demo.util.SPHelper;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

public class ShareInviteActivity extends ShareableActivity  implements IRestoreView {

	private ImageView ivQrcode;
	private AsyncImageView userIcon;
	private TextView userNameTv;
	private TextView userIdTv;
	private View llContent;
	private String capturePath = null;
	private boolean captureIng = false;
	private Bitmap qrBitmap = null;
	private String mobID;
	private RestorePresenter restorePresenter;
	private String avatarUrl;
	private String id;

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_share_invite;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		llContent = findViewById(R.id.moblink_demo_shareLl);
		ivQrcode = (ImageView) findViewById(R.id.moblink_demo_mine_qrcodeIv);
		userIcon = (AsyncImageView) findViewById(R.id.moblink_demo_userIv);
		userNameTv = (TextView) findViewById(R.id.moblink_demo_usernameTv);
		userIdTv = (TextView) findViewById(R.id.moblink_demo_idTv);
		restorePresenter = new RestorePresenter(this);

		userIcon.setRound(ResHelper.dipToPx(this, 360));
		getUserInfo();
	}

	private void getUserInfo() {
		DemoAsyncProtocol.getUserInfo(new DemoAsyncListener<DemoUser>() {
			@Override
			public void onSuccess(DemoUser demoUser) {
				if (null != demoUser && null != demoUser.getRes()) {
					userIcon.execute(demoUser.getRes().avatar, R.drawable.moblink_demo_default_user);
					userNameTv.setText(demoUser.getRes().nickname);
					userIdTv.setText("ID:" + demoUser.getRes().id);
					avatarUrl = demoUser.getRes().avatar;
					id = demoUser.getRes().id;
					getMobIDToCreateQRCode();
				}
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {
				Toast.makeText(ShareInviteActivity.this, "responseCode : " + responseCode + " error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void createQRCode(final String avatarUrl, final String id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String qrUrl = CommonUtils.getShareUrl() + CommonUtils.SHARE_INVITE_PATH + "?id=" + id;
				if (!TextUtils.isEmpty(mobID)) {
					qrUrl += "&mobid=" + mobID;
				}
				try {
					qrBitmap = QRcodeUtils.encodeAsBitmap(qrUrl, 800, 800);

					Bitmap userBm = BitmapHelper.getBitmap(ShareInviteActivity.this, avatarUrl);
					if (null == userBm) {
						userBm = BitmapFactory.decodeResource(getResources(), R.drawable.moblink_demo_default_user);
					}
					qrBitmap = QRcodeUtils.addLogo(qrBitmap, QRcodeUtils.getCircleAvatar(ShareInviteActivity.this, userBm));
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ivQrcode.setImageBitmap(qrBitmap);
							if (qrBitmap == null) {
								Toast.makeText(getApplicationContext(), R.string.txt_moblink_share_qrcode_failed_1, Toast.LENGTH_SHORT).show();
								return;
							}
							captureView(true);
						}
					});
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}).start();
	}

	private void captureView(final boolean isInit) {
		String url = CommonUtils.getShareUrl() + CommonUtils.SHARE_INVITE_PATH + "?id=" + id;
		if (!TextUtils.isEmpty(mobID)) {
			url += "&mobid=" + mobID;
		}
		if (!TextUtils.isEmpty(capturePath)) {
			ShareHelper.showShareReal(ShareInviteActivity.this, null, null, url,mobID, capturePath);
			return;
		}
		if (!isInit && captureIng) {
			Toast.makeText(getApplicationContext(), R.string.txt_moblink_share_qrcode_toast, Toast.LENGTH_SHORT).show();
			return;
		}
		captureIng = true;
		final String finalUrl = url;
		new Thread() {
			public void run() {
				if (isInit) { //第一次初始化，则延迟200秒，保证llContent.getHeight()不会为空
					try {
						Thread.sleep(200);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
				try {
					Bitmap capture = BitmapHelper.captureView(llContent, llContent.getWidth(), llContent.getHeight());
					capturePath = BitmapHelper.saveBitmap(getApplicationContext(), capture);
				} catch (Throwable t) {
					t.printStackTrace();
				}
				captureIng = false;
				if (!isInit) {
					UIHandler.sendEmptyMessage(0, new Handler.Callback() {
						public boolean handleMessage(Message msg) {
							if (TextUtils.isEmpty(capturePath)) {
								Toast.makeText(getApplicationContext(), R.string.txt_moblink_share_qrcode_failed_2, Toast.LENGTH_SHORT).show();
							} else {
								ShareHelper.showShareReal(ShareInviteActivity.this, null,null,
										finalUrl,mobID, capturePath);
							}
							return false;
						}
					});
				}
			}
		}.start();
	}

	@Override
	protected void onRightEvent() {
		rightIv.setEnabled(false);
		captureView(false);
		rightIv.setEnabled(true);
	}

	@Override
	protected int getTitleId() {
		return R.string.moblink_demo_share_invite_title;
	}

	@Override
	public void onMobIdGot(String mobId) {
		if (mobId != null) {
			mobID = mobId;
		} else {
			Toast.makeText(this, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
		}

		createQRCode(avatarUrl, id);
	}

	@Override
	public void onMobIdError(Throwable t) {
		if (t != null) {
			Toast.makeText(this, "error = " + t.getMessage(), Toast.LENGTH_SHORT).show();
		}
		createQRCode(avatarUrl, id);
	}

	private void getMobIDToCreateQRCode() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", SPHelper.getDemoUserId());
		params.put("scene", CommonUtils.SCENE_SHARE_INVITE);
		restorePresenter.getMobId(CommonUtils.SHARE_INVITE_PATH, params);
	}

}
