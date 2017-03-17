package com.mob.moblink.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;

public class ActivityC extends Activity implements View.OnClickListener{

	private ImageView ivBack;
	private TextView tvActTitle;
	private TextView tvActText;
	private Button btnCheckParams;
	private Dialog dialog;
	private String path;
	private String source;
	private String paramStr = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_a);

		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvActTitle = (TextView) findViewById(R.id.tv_act_title);
		tvActText = (TextView) findViewById(R.id.tv_act_text);
		btnCheckParams = (Button) findViewById(R.id.btn_check_params);

		tvActTitle.setText(R.string.activity_c);
		tvActText.setText(R.string.activity_text_c);

		ivBack.setOnClickListener(this);
		btnCheckParams.setOnClickListener(this);

		path = CommonUtils.MAIN_PATH_ARR[2];
	}

	protected void onResume() {
		super.onResume();
		//初始化moblink SDK
		MobLink.initSDK(this, CommonUtils.APPKEY);
		//设置场景还原监听
		MobLink.setIntentHandler(getIntent(), new ActionListener() {
			public void onResult(final HashMap<String, Object> res) {
				runOnUiThread(new Runnable() {
					public void run() {
						if (res != null) {
							if (res.get("source") != null) {
								source = String.valueOf(res.get("source"));
							}
							if (res.get("params") != null) {
								HashMap<String, Object> params = (HashMap<String, Object>) res.get("params");
								for (Map.Entry<String, Object> entry : params.entrySet()) {
									paramStr += (entry.getKey() + " : " + entry.getValue() + "\r\n");
								}
							}
						}

						if (dialog == null) {
							dialog = CommonUtils.getDialog(ActivityC.this, path, source, paramStr);
						}
						if (!dialog.isShowing()) {
							dialog.show();
						}
					}
				});
			}
			public void onError(Throwable t) {
				if (t != null) {
					Toast.makeText(ActivityC.this, t.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back: {
				Intent i = new Intent(this, MainActivity.class);
				i.putExtra("tag", 1);
				startActivity(i);
			} break;
			case R.id.btn_check_params: {
				if (dialog == null) {
					dialog = CommonUtils.getDialog(ActivityC.this, path, source, paramStr);
				}
				if (!dialog.isShowing()) {
					dialog.show();
				}
			} break;
			default:
			break;
		}
	}
}
