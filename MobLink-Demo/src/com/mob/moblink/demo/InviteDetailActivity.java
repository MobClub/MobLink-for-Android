package com.mob.moblink.demo;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.mob.moblink.Scene;

public class InviteDetailActivity extends ShareableActivity {

	private TextView tvInviteTitle;
	private TextView tvInviteText;
	private Scene scene;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_invite_detail);

		tvInviteTitle = (TextView) findViewById(R.id.tv_invite_title);
		tvInviteText = (TextView) findViewById(R.id.tv_invite_text);
		tvInviteTitle.setText(String.format(getString(R.string.invite_register_success), ""));
		fillSceneData(scene);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onReturnSceneData(Scene scene) {
		super.onReturnSceneData(scene);
		this.scene = scene;
	}

	private void fillSceneData(Scene scene) {
		if (scene != null) {
			if (null != scene.getParams()) {
				if (scene.getParams().containsKey("inviteID")) {
					tvInviteText.setText(String.format(getString(R.string.invite_register_person),
							scene.getParams().get("inviteID")));
				}
				if (scene.getParams().containsKey("name")) {
					tvInviteTitle.setText(String.format(getString(R.string.invite_register_success),
							scene.getParams().get("name")));
				}
			}
		}
	}
}
