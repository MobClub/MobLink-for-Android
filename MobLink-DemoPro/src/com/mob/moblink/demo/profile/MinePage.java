package com.mob.moblink.demo.profile;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.demo.R;
import com.mob.moblink.demo.invite.LocalInviteActivity;
import com.mob.moblink.demo.invite.ShareInviteActivity;
import com.mob.moblink.demo.model.DemoUser;
import com.mob.moblink.demo.model.PushInfo;
import com.mob.moblink.demo.util.DemoAsyncListener;
import com.mob.moblink.demo.util.DemoAsyncProtocol;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

public class MinePage implements View.OnClickListener {

	private View view;
	private Context context;
	private AsyncImageView userAvatar;
	private TextView userNickname;
	private TextView getMoneyTv;
	private TextView numTv;
	private boolean isInit;

	public MinePage(View view, Context context) {
		this.view = view;
		this.context = context;

		initView();

		login();
	}

	private void login() {
		DemoAsyncProtocol.getUserInfo(new DemoAsyncListener<DemoUser>() {
			@Override
			public void onSuccess(DemoUser demoUser) {
				if (null != demoUser && null != demoUser.getRes()) {
					isInit = true;
					userAvatar.execute(demoUser.getRes().avatar, R.drawable.moblink_demo_default_user);
					userNickname.setText(demoUser.getRes().nickname);
					getPushInfo();
				}
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {
				Toast.makeText(context, "responseCode : " + responseCode + " error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void onResume() {
		if (!isInit) {
			login();
		}
	}

	private void getPushInfo() {
		DemoAsyncProtocol.queryPushInfo(new DemoAsyncListener<PushInfo>() {
			@Override
			public void onSuccess(PushInfo pushInfo) {
				getMoneyTv.setText(pushInfo.getRes().getMoney+"");
				numTv.setText(pushInfo.getRes().num+"");
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {
				Toast.makeText(context, "responseCode : " + responseCode + " error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void initView() {
		view.findViewById(R.id.moblink_demo_localLl).setOnClickListener(this);
		view.findViewById(R.id.moblink_demo_shareLl).setOnClickListener(this);
		view.findViewById(R.id.moblink_demo_friendLl).setOnClickListener(this);
		userAvatar = (AsyncImageView) view.findViewById(R.id.moblink_demo_userIv);
		userNickname = (TextView) view.findViewById(R.id.moblink_demo_usernameTv);
		getMoneyTv = (TextView) view.findViewById(R.id.moblink_demo_getMoneyTv);
		numTv = (TextView) view.findViewById(R.id.moblink_demo_numTv);

		userAvatar.setRound(ResHelper.dipToPx(context, 360));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_localLl:
				context.startActivity(new Intent(context, LocalInviteActivity.class));
				break;
			case R.id.moblink_demo_shareLl:
				context.startActivity(new Intent(context, ShareInviteActivity.class));
				break;
			case R.id.moblink_demo_friendLl:
				context.startActivity(new Intent(context, FriendActivity.class));
				break;
		}
	}
}
