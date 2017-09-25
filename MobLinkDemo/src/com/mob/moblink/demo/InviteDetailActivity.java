package com.mob.moblink.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.mob.moblink.Scene;
import com.mob.moblink.SceneRestorable;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.Map;

public class InviteDetailActivity extends BaseActivity implements SceneRestorable {

	private TextView tvInviteTitle;
	private TextView tvInviteText;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_invite_detail);

		tvInviteTitle = (TextView) findViewById(R.id.tv_invite_title);
		tvInviteText = (TextView) findViewById(R.id.tv_invite_text);
		tvInviteTitle.setText(String.format(getString(R.string.invite_register_success), ""));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onReturnSceneData(Scene scene) {
		if (scene != null) {
			path = scene.path;
			source = scene.source;
			paramStr = "";
			if (scene.params != null) {
				for (Map.Entry<String, Object> entry : scene.params.entrySet()) {
					paramStr += (entry.getKey() + " : " + entry.getValue() + "\r\n");
				}
			}

			// dialog不能复用, 防止参数更换, 不能及时更新
			if (null != dialog && dialog.isShowing()) {
				dialog.dismiss();
			}
			dialog = CommonUtils.getDialog(this, scene.path, scene.source, paramStr);
			if (!dialog.isShowing()) {
				dialog.show();
			}

			if (null != scene.params) {
				if (scene.params.containsKey("inviteID")) {
					tvInviteText.setText(String.format(getString(R.string.invite_register_person),
							scene.params.get("inviteID")));
				}
				if (scene.params.containsKey("name")) {
					tvInviteTitle.setText(String.format(getString(R.string.invite_register_success),
							scene.params.get("name")));
				}
			}
		}
	}
}
