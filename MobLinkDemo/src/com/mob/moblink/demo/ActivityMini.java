package com.mob.moblink.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mob.moblink.Scene;
import com.mob.moblink.SceneRestorable;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.Map;

public class ActivityMini extends BaseActivity implements SceneRestorable {

	private TextView tvActTitle;
	private TextView tvActText;
	private Button btnCheckParams;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mini);

		Intent intent = getIntent();
		if (null != intent && null != intent.getData()) {
			path = intent.getData().getPath();
		}

		tvActTitle = (TextView) findViewById(R.id.tv_act_title);
		tvActText = (TextView) findViewById(R.id.tv_act_text);
		btnCheckParams = (Button) findViewById(R.id.btn_check_params);

		btnCheckParams.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_check_params: {
				showParamDialog();
			} break;
			default: {
				super.onClick(v);
			} break;
		}
	}

	@Override
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
		}
	}
}
