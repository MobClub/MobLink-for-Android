package com.mob.moblink.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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

import cn.sharesdk.onekeyshare.OnekeyShare;

public class InviteDetailActivity extends Activity implements View.OnClickListener{

	private ImageView ivBack;
	private TextView tvInviteTitle;
	private TextView tvInviteText;
	private Dialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_invite_detail);

		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvInviteTitle = (TextView) findViewById(R.id.tv_invite_title);
		tvInviteText = (TextView) findViewById(R.id.tv_invite_text);
		tvInviteTitle.setText(String.format(getString(R.string.invite_register_success), ""));
		ivBack.setOnClickListener(this);
	}

	protected void onResume() {
		super.onResume();
		MobLink.initSDK(this, CommonUtils.APPKEY);
		MobLink.setIntentHandler(getIntent(), new ActionListener() {
			public void onResult(final HashMap<String, Object> res) {
				runOnUiThread(new Runnable() {
					public void run() {
						String source = "";
						String paramStr = "";
						if (res != null) {
							if (res.get("source") != null) {
								source = String.valueOf(res.get("source"));
							}
							if (res.get("params") != null) {
								HashMap<String, Object> params = (HashMap<String, Object>) res.get("params");
								for (Map.Entry<String, Object> entry : params.entrySet()) {
									if ("inviteID".equals(entry.getKey())) {
										tvInviteText.setText(String.format(getString(R.string.invite_register_person), entry.getValue()));
									} else if ("name".equals(entry.getKey())) {
										tvInviteTitle.setText(String.format(getString(R.string.invite_register_success), entry.getValue()));
									}
									paramStr += (entry.getKey() + " : " + entry.getValue() + "\r\n");
								}
							}
						}

						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						dialog = CommonUtils.getDialog(InviteDetailActivity.this, CommonUtils.INVITE_PATH, source, paramStr);
						dialog.show();
					}
				});
			}
			public void onError(Throwable t) {
				if (t != null) {
					Toast.makeText(InviteDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});

		setIntent(null);
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back: {
				Intent i = new Intent(this, MainActivity.class);
				i.putExtra("tag", 3);
				startActivity(i);
			} break;
			default:
			break;
		}
	}

}
