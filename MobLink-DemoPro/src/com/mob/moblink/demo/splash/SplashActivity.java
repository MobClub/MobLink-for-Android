package com.mob.moblink.demo.splash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.mob.PrivacyPolicy;
import com.mob.moblink.MobLink;
import com.mob.moblink.demo.DemoApplication;
import com.mob.moblink.demo.MainActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.util.PrefUtils;
import com.mob.moblink.demo.util.PrivacyDialog;
import com.mob.moblink.demo.util.SPHelper;
import com.mob.moblink.demo.util.SpConstant;
import com.mob.moblink.demo.util.statusbar.StatusBarCompat;
import com.mob.tools.gui.MobViewPager;

import org.jetbrains.annotations.Nullable;

public class SplashActivity extends Activity implements ISplashView, View.OnClickListener {

	private MobViewPager mobViewPager;
	private SplashViewPagerAdapter splashViewPagerAdapter;
	private int[] indicatorIds = new int[]{R.id.moblink_demo_indicator1, R.id.moblink_demo_indicator2, R.id.moblink_demo_indicator3};
	private static final int RESULT_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean isAuth = PrefUtils.getBoolean(SpConstant.ISAUTH, false, this);

		if (SPHelper.demoHasFirst() && isAuth) {
			gotoMainActivity();
		}
		setContentView(R.layout.moblink_demo_activity_splash);

		initView();
		MobLink.skipRestoreSceneFromWx(SplashActivity.class);

		if (!isAuth) {
            /*Intent intent = new Intent();
            intent.setClass(this, PrivacyDialog.class);
            startActivityForResult(intent, RESULT_CODE);*/
			dialogPrivate();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//首次进入弹出隐私协议
	}

	Dialog dialog_private = null;

	public void dialogPrivate() {
		dialog_private = new Dialog(this, R.style.ActionSheetDialogStyle_center);
		View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_pri, null);
		dialog_private.setContentView(inflate);
		final TextView showContent = inflate.findViewById(R.id.show_content);
		inflate.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog_private.dismiss();
				PrefUtils.putBoolean(SpConstant.ISAUTH, false, SplashActivity.this);
				submitPrivacyGrantResult(false);
				android.os.Process.killProcess(android.os.Process.myPid());
				// finish();
			}
		});
		inflate.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submitPrivacyGrantResult(true);
				PrefUtils.putBoolean(SpConstant.ISAUTH, true, SplashActivity.this);
				dialog_private.dismiss();
			}
		});
		// 异步方法
		MobSDK.getPrivacyPolicyAsync(MobSDK.POLICY_TYPE_URL, new PrivacyPolicy.OnPolicyListener() {
			@Override
			public void onComplete(PrivacyPolicy data) {
				if (data != null) {
					// 富文本内容
					String text = data.getContent();
					Log.d("Moblink", "text==" + text);
					if (showContent != null) {
						showContent.setText(
								MobSDK.getContext().getResources().getString(R.string.privacy_content) + "\n" +
										MobSDK.getContext().getResources().getString(R.string.privacy_details) + " "
										+ Html.fromHtml(text));

					}
				}
			}

			@Override
			public void onFailure(Throwable t) {
			}
		});
		Window dialogWindow = dialog_private.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
		dialog_private.setCancelable(false);
		if (!dialog_private.isShowing()) {
			dialog_private.show();
		}
	}

	private void submitPrivacyGrantResult(boolean granted) {
		MobSDK.submitPolicyGrantResult(granted, new OperationCallback<Void>() {
			@Override
			public void onComplete(Void data) {
				Log.e("Moblink", "隐私协议授权结果提交：成功 " + data);
			}

			@Override
			public void onFailure(Throwable t) {
				Log.e("Moblink", "隐私协议授权结果提交：失败: " + t);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
		if (requestCode == 1 && resultCode == 3) {
			finish();
			System.exit(0);
		}
	}

	private void initView() {
		mobViewPager = (MobViewPager) findViewById(R.id.moblink_demo_splash_mobviewpager);
		findViewById(R.id.moblink_demo_splash_skip).setOnClickListener(this);
		findViewById(indicatorIds[0]).setSelected(true);
		initData();
	}

	private void initData() {
		splashViewPagerAdapter = new SplashViewPagerAdapter(this, this);
		mobViewPager.setAdapter(splashViewPagerAdapter);
	}

	@Override
	public void onScreenChange(int currentScreen, int lastScreen) {
		for (int i = 0; i < indicatorIds.length; i++) {
			if (currentScreen == i) {
				findViewById(indicatorIds[i]).setSelected(true);
			} else {
				findViewById(indicatorIds[i]).setSelected(false);
			}
		}
	}

	@Override
	public void gotoMainActivity() {
		startActivity(new Intent(this, MainActivity.class));
		SPHelper.setDemoFirst(true);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.moblink_demo_splash_skip:
				gotoMainActivity();
				break;
		}
	}
}
