package com.mob.moblink.demo.restore.game;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.Scene;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.ShareableActivity;
import com.mob.moblink.demo.common.ShareHelper;
import com.mob.moblink.demo.model.DemoUser;
import com.mob.moblink.demo.model.JoinRoom;
import com.mob.moblink.demo.restore.presenter.RestorePresenter;
import com.mob.moblink.demo.restore.view.IRestoreView;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.DemoAsyncListener;
import com.mob.moblink.demo.util.DemoAsyncProtocol;
import com.mob.moblink.demo.util.SPHelper;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;

public class GameActivity extends ShareableActivity implements IRestoreView {

	private AsyncImageView userIv1;
	private AsyncImageView userIv2;
	private TextView userNameTv1;
	private TextView userNameTv2;
	private Integer roomId;
	private Handler handler;
	private String mobID;
	private RestorePresenter restorePresenter;

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_game;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		findViewById(R.id.moblink_demo_title_bar).setVisibility(View.GONE);
		restorePresenter = new RestorePresenter(this);

		userIv1 = (AsyncImageView) findViewById(R.id.moblink_demo_game_userIv1);
		userIv2 = (AsyncImageView) findViewById(R.id.moblink_demo_game_userIv2);
		userNameTv1 = (TextView) findViewById(R.id.moblink_demo_game_userTv1);
		userNameTv2 = (TextView) findViewById(R.id.moblink_demo_game_userTv2);

		userIv1.setRound(ResHelper.dipToPx(this, 360));
		userIv2.setRound(ResHelper.dipToPx(this, 360));
		userIv1.setImageBitmap(CommonUtils.getCircleAvatar(GameActivity.this,
				BitmapFactory.decodeResource(getResources(), R.drawable.moblink_demo_game_user_none)));
		userIv2.setImageBitmap(CommonUtils.getCircleAvatar(GameActivity.this,
				BitmapFactory.decodeResource(getResources(), R.drawable.moblink_demo_game_user_none)));

		findViewById(R.id.moblink_demo_game_share).setOnClickListener(this);

		initHandler();
		joinRoom();
	}

	private void joinRoom() {
		DemoAsyncProtocol.joinRoom(roomId, new DemoAsyncListener<JoinRoom>() {
			@Override
			public void onSuccess(JoinRoom joinRoom) {
				if (null != joinRoom.getRes()) {
					roomId = joinRoom.getRes().roomId;
					for (int i = 0; i < joinRoom.getRes().user.size(); i++) {
						DemoUser user = joinRoom.getRes().user.get(i);
						if (i == 0) {
							userIv1.execute(user.avatar, R.drawable.moblink_demo_default_user);
							userIv1.setTag(user.avatar);
							userNameTv1.setText(user.nickname);
						} else if (i == 1) {
							userIv2.execute(user.avatar, R.drawable.moblink_demo_default_user);
							userIv2.setTag(user.avatar);
							userNameTv2.setText(user.nickname);
						}
					}
					handler.sendEmptyMessageDelayed(0, 3000);
				} else {
					roomId = null;
					joinRoom();
				}
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {
				Toast.makeText(GameActivity.this, "responseCode : " + responseCode + " error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void initHandler() {
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				readRoom();
				handler.sendEmptyMessageDelayed(0, 3000);
				return false;
			}
		});
	}

	@Override
	protected int getTitleBarColor() {
		return 0xff57a8d3;
	}

	private void readRoom() {
		DemoAsyncProtocol.readRoom(roomId, new DemoAsyncListener<JoinRoom>() {
			@Override
			public void onSuccess(JoinRoom joinRoom) {
				if (null == joinRoom || null == joinRoom.user) {
					return;
				}
				for (int i = 0; i < joinRoom.user.size(); i++) {
					DemoUser user = joinRoom.user.get(i);
					if (i == 0) {
						if (!user.avatar.equals(userIv1.getTag())) {
							userIv1.execute(user.avatar, R.drawable.moblink_demo_default_user);
							userIv1.setTag(user.avatar);
						}
						userNameTv1.setText(user.nickname);
					} else if (i == 1) {
						if (!user.avatar.equals(userIv2.getTag())) {
							userIv2.execute(user.avatar, R.drawable.moblink_demo_default_user);
							userIv2.setTag(user.avatar);
						}
						userNameTv2.setText(user.nickname);
					}
				}
				switch (joinRoom.user.size()) {
					case 0:
						userNameTv1.setText("");
						userIv1.setTag("");
						userIv1.execute("", CommonUtils.getCircleAvatar(GameActivity.this,
								BitmapFactory.decodeResource(getResources(), R.drawable.moblink_demo_game_user_none)));
					case 1:
						userNameTv2.setText("");
						userIv2.setTag("");
						userIv2.execute("", CommonUtils.getCircleAvatar(GameActivity.this,
								BitmapFactory.decodeResource(getResources(), R.drawable.moblink_demo_game_user_none)));
						break;
				}
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {

			}
		});
	}

	@Override
	public void onReturnSceneData(Scene scene) {
		super.onReturnSceneData(scene);
		if (null != scene && null != scene.getParams()) {
			if (scene.getParams().containsKey("roomId")) {
				roomId = Integer.valueOf(String.valueOf(scene.getParams().get("roomId")));
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeMessages(0);
		exitRoom();
	}

	private void exitRoom() {
		DemoAsyncProtocol.exitRoom(new DemoAsyncListener<Integer>() {
			@Override
			public void onSuccess(Integer integer) {

			}

			@Override
			public void onFailure(int responseCode, Throwable e) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_game_share:
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
			Toast.makeText(GameActivity.this, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
		}
		share();
	}

	@Override
	public void onMobIdError(Throwable t) {
		if (t != null) {
			Toast.makeText(GameActivity.this, "error = " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
		params.put("scene", CommonUtils.SCENE_GAME);
		restorePresenter.getMobId(CommonUtils.GAME_PATH, params);
	}

	private void share() {
		String imgPath = CommonUtils.copyImgToSD(this, R.drawable.moblink_demo_icon, "share_link_icon");
		String title = getString(R.string.moblink_demo_game_share_tilte);
		String text = getString(R.string.moblink_demo_game_share_text);
		String shareUrl = CommonUtils.getShareUrl() + CommonUtils.GAME_PATH + "?id=" + SPHelper.getDemoUserId() + "&room=" + roomId;

		if (!TextUtils.isEmpty(mobID)) {
			shareUrl += "&mobid=" + mobID;
		}
		ShareHelper.showShareReal(this, title, text, shareUrl, imgPath);
	}
}
