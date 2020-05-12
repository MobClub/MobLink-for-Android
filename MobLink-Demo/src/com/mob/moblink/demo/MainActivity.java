package com.mob.moblink.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.Scene;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.HashMap;

public class MainActivity extends BaseActivity {

	private static Activity sFirstInstance;
	private Context context;

	private static final int[] TAB_INDEX = new int[]{
			R.id.ll_tag1,
			R.id.ll_tag2,
			R.id.ll_tag3,
			R.id.ll_tag4
	};
	private static final int[] TAB_CONTENT = new int[]{
			R.id.layout_tag1,
			R.id.layout_tag2,
			R.id.layout_tag3,
			R.id.layout_tag4
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (null == sFirstInstance) {
			sFirstInstance = this;
			setContentView(R.layout.activity_main);
			initView();
		} else if (this != sFirstInstance) {
			// 防止微信跳转过来，多个MainActivity界面(是singletop)
			finish();
		} else {
			setContentView(R.layout.activity_main);
			initView();
		}
	}

	private void initView() {
		context = this.getApplicationContext();
		for (int i = 0; i < TAB_INDEX.length; i++) {
			View v = findViewById(TAB_INDEX[i]);
			v.setOnClickListener(this);
		}
		new DemoPage(findViewById(R.id.layout_tag1));
		new ScenePage(findViewById(R.id.layout_tag2));
		new ParamPage(findViewById(R.id.layout_tag3));
		new DemoPage(findViewById(R.id.layout_tag4));
		switchTab(R.id.ll_tag1);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getIntent() != null) {
			int tag = getIntent().getIntExtra("tag", -1);
			if (tag >= 0 && tag < TAB_INDEX.length) {
				switchTab(TAB_INDEX[tag]);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_tag1:
			case R.id.ll_tag2:
			case R.id.ll_tag3:
			case R.id.ll_tag4: {
				switchTab(v.getId());
			} break;
			default: {
				super.onClick(v);
			} break;
		}
	}

	private void switchTab(int tagId) {
		for (int i = 0; i < TAB_INDEX.length; i++) {
			View tab = findViewById(TAB_INDEX[i]);
			View content = findViewById(TAB_CONTENT[i]);
			if (TAB_INDEX[i] == tagId) {
				tab.setSelected(true);
				content.setVisibility(View.VISIBLE);
			} else {
				tab.setSelected(false);
				content.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (sFirstInstance == this) {
			sFirstInstance = null;
		}
	}

	/**
	 * 回退时, 必须时拉起launcher.
	 */
	protected static void launcherMainIfNecessary(Activity current) {
		if (null == sFirstInstance) {
			Intent intent = new Intent(current, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			current.startActivity(intent);
		}
	}

	class DemoPage extends Object implements View.OnClickListener {

		private View rootView;
		private Button btnShare;

		private String mobID;

		public DemoPage(View view) {
			super();
			rootView = view;
			initView();
		}

		private void initView() {
			findViewById(R.id.btn_get_mobid).setOnClickListener(this);
			findViewById(R.id.tv_set_default_value).setOnClickListener(this);


			btnShare = (Button) findViewById(R.id.btn_share);
			if (null == btnShare) {
				btnShare = (Button) findViewById(R.id.btn_share_to_wxmini);
			}

			btnShare.setSelected(false);
			btnShare.setOnClickListener(this);
		}

		private View findViewById(int id) {
			return rootView.findViewById(id);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_get_mobid: {
					//获取MobID
					Spinner spPath = (Spinner) findViewById(R.id.sp_path);
					Scene s = new Scene();
					if (null != spPath) {
						int selectedID = spPath.getSelectedItemPosition();
						s.setPath(CommonUtils.MAIN_PATH_ARR[selectedID]);
					} else {
						s.setPath(getString(R.string.path_wx_mini));
					}
					s.setParams(collectParams());
					MobLink.getMobID(s, new ActionListener<String>() {
						@Override
						public void onResult(String mobID) {
							btnShare.setSelected(true);
							DemoPage.this.mobID = mobID;
							Toast.makeText(context, "Get MobID Successfully!", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onError(Throwable t) {
							btnShare.setSelected(true);
							Toast.makeText(context, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
						}
					});
				} break;
				case R.id.btn_share: {
					if (TextUtils.isEmpty(mobID)) {
						CommonUtils.getMobIdDialog(MainActivity.this).show();
						return;
					}
					//演示界面的分享功能
					String shareUrl = CommonUtils.getAppLinkShareUrl() + "/" + mobID;
					String title = getString(R.string.show_share_titel);
					String text = getString(R.string.share_text);
					String imgPath = CommonUtils.copyImgToSD(MainActivity.this, R.drawable.demo_share_moblink, "moblink");
					CommonUtils.showShare(MainActivity.this, title, text, shareUrl, imgPath);
				} break;
				case R.id.btn_share_to_wxmini: {
					if (TextUtils.isEmpty(mobID)) {
						CommonUtils.getMobIdDialog(MainActivity.this).show();
						return;
					}
					CommonUtils.showShareWxMini(MainActivity.this, mobID);
				} break;
				case R.id.tv_set_default_value: {
					//填充默认值
					final int[] tvKeys = new int[]{
							R.id.et_key1,
							R.id.et_key2,
							R.id.et_key3
					};
					final int[] tvValues = new int[] {
							R.id.et_value1,
							R.id.et_value2,
							R.id.et_value3
					};
					final int[] keys = new int[] {
							R.string.key1,
							R.string.key2,
							R.string.key3
					};
					final int[] values = new int[] {
							R.string.value1,
							R.string.value2,
							R.string.value3
					};

					for (int i = 0; i < tvKeys.length; i++) {
						TextView tv = (TextView)findViewById(tvKeys[i]);
						tv.setText(keys[i]);
						tv = (TextView)findViewById(tvValues[i]);
						tv.setText(values[i]);
					}
				} break;
			}
		}

		public HashMap<String, Object> collectParams() {
			HashMap<String, Object> params = new HashMap<String, Object>();
			final int[] keys = new int[]{
					R.id.et_key1,
					R.id.et_key2,
					R.id.et_key3
			};
			final int[] values = new int[] {
					R.id.et_value1,
					R.id.et_value2,
					R.id.et_value3
			};

			for (int i = 0; i < keys.length; i++) {
				TextView tvKey = (TextView)findViewById(keys[i]);
				TextView tvValue = (TextView)findViewById(values[i]);
				params.put(tvKey.getText().toString(), tvValue.getText().toString());
			}
			return params;
		}
	}

	class ScenePage extends Object implements View.OnClickListener {
		private View rootView;

		public ScenePage(View view) {
			super();
			rootView = view;
			initView();
		}

		private void initView() {
			findViewById(R.id.ll_news).setOnClickListener(this);
			findViewById(R.id.ll_videos).setOnClickListener(this);
			findViewById(R.id.ll_shopping).setOnClickListener(this);
		}

		private View findViewById(int id) {
			return rootView.findViewById(id);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ll_news: {
					//资讯APP，打开新闻列表
					Intent intent = new Intent(MainActivity.this, NewsActivity.class);
					startActivity(intent);
				} break;
				case R.id.ll_videos: {
					//视频APP，打开视频列表
					Intent intent = new Intent(MainActivity.this, VideosActivity.class);
					startActivity(intent);
				} break;
				case R.id.ll_shopping: {
					//电商APP，打开商品列表
					Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
					startActivity(intent);
				} break;
			}
		}
	}

	class ParamPage extends Object implements View.OnClickListener {
		private View rootView;

		public ParamPage(View view) {
			super();
			rootView = view;
			initView();
		}

		private void initView() {
			findViewById(R.id.ll_air_ticket).setOnClickListener(this);
			findViewById(R.id.ll_invite_users).setOnClickListener(this);
		}

		private View findViewById(int id) {
			return rootView.findViewById(id);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ll_air_ticket: {
					//飞机票APP，打开飞机票选择界面
					Intent intent = new Intent(MainActivity.this, TicketActivity.class);
					startActivity(intent);
				} break;
				case R.id.ll_invite_users: {
					//打开邀请用户界面
					Intent intent = new Intent(MainActivity.this, InviteActivity.class);
					startActivity(intent);
				} break;
			}
		}
	}
}
