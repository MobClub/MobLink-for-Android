package com.mob.moblink.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class MainActivity extends Activity implements OnClickListener{

	private Context context;
	private Spinner spPath;
	private EditText etSource;
	private EditText etKey1;
	private EditText etKey2;
	private EditText etKey3;
	private EditText etValue1;
	private EditText etValue2;
	private EditText etValue3;
	private Button btnShare;
	private Button btnGetMobID;
	private TextView tvSetDefValue;
	private View tag1;
	private View tag2;
	private View tag3;
	private View layoutTag1;
	private View layoutTag2;
	private View layoutTag3;
	private View llNews;
	private View llVideos;
	private View llShopping;
	private View llAirTicket;
	private View llInviteUser;

	private String mobID;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		initData(getIntent());
	}

	private void initView() {
		context = this.getApplicationContext();
		tag1 = findViewById(R.id.ll_tag1);
		tag2 = findViewById(R.id.ll_tag2);
		tag3 = findViewById(R.id.ll_tag3);
		layoutTag1 = findViewById(R.id.layout_tag1);
		layoutTag2 = findViewById(R.id.layout_tag2);
		layoutTag3 = findViewById(R.id.layout_tag3);
		//底部工具类
		tag1.setSelected(true);
		tag1.setOnClickListener(this);
		tag2.setOnClickListener(this);
		tag3.setOnClickListener(this);
		layoutTag1.setVisibility(View.VISIBLE);
		layoutTag2.setVisibility(View.GONE);
		layoutTag3.setVisibility(View.GONE);
		//第一个tag的view
		spPath = (Spinner)findViewById(R.id.sp_path);
		etKey1 = (EditText) findViewById(R.id.et_key1);
		etKey2 = (EditText) findViewById(R.id.et_key2);
		etKey3 = (EditText) findViewById(R.id.et_key3);
		etValue1 = (EditText) findViewById(R.id.et_value1);
		etValue2 = (EditText) findViewById(R.id.et_value2);
		etValue3 = (EditText) findViewById(R.id.et_value3);
		etSource = (EditText) findViewById(R.id.et_source);
		btnGetMobID = (Button) findViewById(R.id.btn_get_mobid);
		btnShare = (Button) findViewById(R.id.btn_share);
		tvSetDefValue = (TextView) findViewById(R.id.tv_set_default_value);

		btnShare.setSelected(false);
		btnShare.setOnClickListener(this);
		btnGetMobID.setOnClickListener(this);
		tvSetDefValue.setOnClickListener(this);
		//第二个tag的view
		llNews = findViewById(R.id.ll_news);
		llVideos = findViewById(R.id.ll_videos);
		llShopping = findViewById(R.id.ll_shopping);
		llNews.setOnClickListener(this);
		llVideos.setOnClickListener(this);
		llShopping.setOnClickListener(this);
		//第三个tag的view
		llAirTicket = findViewById(R.id.ll_air_ticket);
		llInviteUser = findViewById(R.id.ll_invite_users);
		llAirTicket.setOnClickListener(this);
		llInviteUser.setOnClickListener(this);
	}

	protected void onResume() {
		super.onResume();
		if (getIntent() != null) {
			int tag = getIntent().getIntExtra("tag", 0);
			if (tag == 1) {
				onClick(tag1);
			} else if (tag == 2) {
				onClick(tag2);
			} else if (tag == 3) {
				onClick(tag3);
			}
		}
	}

	private void initData(Intent intent) {
		ShareSDK.initSDK(this);
		MobLink.initSDK(this, CommonUtils.APPKEY);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_tag1:{
				//演示，tag工具栏的点击处理
				layoutTag1.setVisibility(View.VISIBLE);
				layoutTag2.setVisibility(View.GONE);
				layoutTag3.setVisibility(View.GONE);
				tag1.setSelected(true);
				tag2.setSelected(false);
				tag3.setSelected(false);
			} break;
			case R.id.ll_tag2:{
				//常见应用场景，tag工具栏的点击处理
				layoutTag2.setVisibility(View.VISIBLE);
				layoutTag1.setVisibility(View.GONE);
				layoutTag3.setVisibility(View.GONE);
				tag2.setSelected(true);
				tag1.setSelected(false);
				tag3.setSelected(false);
			} break;
			case R.id.ll_tag3:{
				//传入参数场景，tag工具栏的点击处理
				layoutTag3.setVisibility(View.VISIBLE);
				layoutTag1.setVisibility(View.GONE);
				layoutTag2.setVisibility(View.GONE);
				tag3.setSelected(true);
				tag1.setSelected(false);
				tag2.setSelected(false);
			} break;
			case R.id.btn_get_mobid:{
				//获取MobID
				HashMap<String, Object> params = new HashMap<String, Object>();
				String source = etSource.getText().toString();
				int selectedID = spPath.getSelectedItemPosition();
				String key1 = etKey1.getText().toString();
				String key2 = etKey2.getText().toString();
				String key3 = etKey3.getText().toString();
				String value1 = etValue1.getText().toString();
				String value2 = etValue2.getText().toString();
				String value3 = etValue3.getText().toString();
				params.put(key1, value1);
				params.put(key2, value2);
				params.put(key3, value3);

				MobLink.getMobID(params, CommonUtils.MAIN_PATH_ARR[selectedID], source, new ActionListener() {
					public void onResult(HashMap<String, Object> params) {
						if (params != null && params.containsKey("mobID")) {
							btnShare.setSelected(true);
							mobID = String.valueOf(params.get("mobID"));
						} else {
							Toast.makeText(context, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
						}
					}

					public void onError(Throwable t) {
						btnShare.setSelected(true);
						if (t != null) {
							Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}
				});
			} break;
			case R.id.btn_share:{
				if (TextUtils.isEmpty(mobID)) {
					CommonUtils.getMobIdDialog(this).show();
					return;
				}
				//演示界面的分享功能
				int selectedID = spPath.getSelectedItemPosition();
				String shareUrl = CommonUtils.SHARE_URL + CommonUtils.MAIN_PATH_ARR[selectedID];
				if (!TextUtils.isEmpty(mobID)) {
					shareUrl += "/?mobid=" + mobID;
				}
				String title = getString(R.string.show_share_titel);
				String text = getString(R.string.share_text);
				String imgPath = CommonUtils.copyImgToSD(this, R.drawable.demo_share_moblink , "moblink");
				CommonUtils.showShare(this, title, text, shareUrl, imgPath);
			} break;
			case  R.id.tv_set_default_value:{
				//填充默认值
				etSource.setText(R.string.app_name);
				etKey1.setText(R.string.key1);
				etKey2.setText(R.string.key2);
				etKey3.setText(R.string.key3);
				etValue1.setText(R.string.value1);
				etValue2.setText(R.string.value2);
				etValue3.setText(R.string.value3);
			} break;
			case R.id.ll_news:{
				//资讯APP，打开新闻列表
				Intent intent = new Intent(MainActivity.this, NewsActivity.class);
				startActivity(intent);
			} break;
			case R.id.ll_videos:{
				//视频APP，打开视频列表
				Intent intent = new Intent(MainActivity.this, VideosActivity.class);
				startActivity(intent);
			} break;
			case R.id.ll_shopping:{
				//电商APP，打开商品列表
				Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
				startActivity(intent);
			} break;
			case R.id.ll_air_ticket:{
				//飞机票APP，打开飞机票选择界面
				Intent intent = new Intent(MainActivity.this, TicketActivity.class);
				startActivity(intent);
			} break;
			case R.id.ll_invite_users:{
				//打开邀请用户界面
				Intent intent = new Intent(MainActivity.this, InviteActivity.class);
				startActivity(intent);
			} break;
			default:
			break;
		}
	}

}
