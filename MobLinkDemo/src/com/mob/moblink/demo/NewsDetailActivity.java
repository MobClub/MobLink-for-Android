package com.mob.moblink.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class NewsDetailActivity extends Activity implements View.OnClickListener{

	private ImageView ivBack;
	private TextView tvShare;
	private View llNewsDetail ;
	private TextView tvNewsTime;
	private TextView tvNewsTitle;
	private TextView tvNewsDetail;
	private View newsSug01;
	private View newsSug02;
	private View newsSug03;

	private Dialog dialog;

	private int newsID;
	private String mobID;
	private int newsShareIcon;
	private HashMap<Integer, String> mobIdCache; //mobID缓存

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvShare = (TextView) findViewById(R.id.tv_share);
		llNewsDetail = findViewById(R.id.ll_news_detail);
		tvNewsTitle = (TextView) llNewsDetail.findViewById(R.id.tv_news_title);
		tvNewsDetail = (TextView) llNewsDetail.findViewById(R.id.tv_news_detail);
		tvNewsTime = (TextView) llNewsDetail.findViewById(R.id.tv_news_time);
		newsSug01 = findViewById(R.id.rl_news_sug01);
		newsSug02 = findViewById(R.id.rl_news_sug02);
		newsSug03 = findViewById(R.id.rl_news_sug03);

		ivBack.setOnClickListener(this);
		tvShare.setOnClickListener(this);
		newsSug01.setOnClickListener(this);
		newsSug02.setOnClickListener(this);
		newsSug03.setOnClickListener(this);

		mobIdCache = new HashMap<Integer, String>();
	}

	protected void onResume() {
		super.onResume();
		if (getIntent() != null) {
			newsID = getIntent().getIntExtra("position", newsID);
			setNewsDetail();
		}
		MobLink.initSDK(this, CommonUtils.APPKEY);
		MobLink.setIntentHandler(getIntent(), new ActionListener() {
			public void onResult(final HashMap<String, Object> res) {
				runOnUiThread(new Runnable() {
					public void run() {
						String sourceStr = null;
						String paramStr = "";
						if (res != null) {
							if (res.get("source") != null) {
								sourceStr = String.valueOf(res.get("source"));
							}
							if (res.get("params") != null) {
								HashMap<String, Object> params = (HashMap<String, Object>) res.get("params");
								for (Map.Entry<String, Object> entry : params.entrySet()) {
									if ("newsID".equals(entry.getKey()) || "id".equals(entry.getKey())) {
										newsID = Integer.parseInt(String.valueOf(entry.getValue()));
										setNewsDetail();
										paramStr += (entry.getKey() + " : " + entry.getValue() + "\r\n");
									}
								}
							}
						}

						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						dialog = CommonUtils.getDialog(NewsDetailActivity.this, CommonUtils.NEWS_PATH, sourceStr, paramStr);
						dialog.show();
					}
				});
			}
			public void onError(Throwable t) {
				if (t != null) {
					Toast.makeText(NewsDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
				Intent i = new Intent(this, NewsActivity.class);
				startActivity(i);
			} break;
			case R.id.tv_share: {
				//分享
				getMobIDToShare();
			} break;
			case R.id.rl_news_sug01: {
				newsID = 100;
				setNewsDetail();
			} break;
			case R.id.rl_news_sug02: {
				newsID = 101;
				setNewsDetail();
			} break;
			case R.id.rl_news_sug03: {
				newsID = 102;
				setNewsDetail();
			} break;
			default:
			break;
		}
	}

	private void setNewsDetail() {
		if (newsID < 100) {
			HashMap<String, Object> news =  CommonUtils.getNewsData(this).get(newsID);
			tvNewsTitle.setText((String) news.get("title"));
			tvNewsDetail.setText((String) news.get("detail"));
			tvNewsTime.setText((String) news.get("time"));
			newsShareIcon = (Integer) news.get("img");
		} else if (newsID == 100) {
			tvNewsTitle.setText(R.string.news_sug01_title);
			tvNewsDetail.setText(R.string.news_sug01_detail);
			tvNewsTime.setText(R.string.news_sug01_time);
			newsSug01.setVisibility(View.GONE);
			newsSug02.setVisibility(View.VISIBLE);
			newsSug03.setVisibility(View.VISIBLE);
			newsShareIcon = R.drawable.demo_news_sug01;
		} else if (newsID == 101) {
			tvNewsTitle.setText(R.string.news_sug02_title);
			tvNewsDetail.setText(R.string.news_sug02_detail);
			tvNewsTime.setText(R.string.news_sug02_time);
			newsSug01.setVisibility(View.VISIBLE);
			newsSug02.setVisibility(View.GONE);
			newsSug03.setVisibility(View.VISIBLE);
			newsShareIcon = R.drawable.demo_news_sug02;
		} else if (newsID == 102) {
			tvNewsTitle.setText(R.string.news_sug03_title);
			tvNewsDetail.setText(R.string.news_sug03_detail);
			tvNewsTime.setText(R.string.news_sug03_time);
			newsSug01.setVisibility(View.VISIBLE);
			newsSug02.setVisibility(View.VISIBLE);
			newsSug03.setVisibility(View.GONE);
			newsShareIcon = R.drawable.demo_news_sug03;
		}
	}

	private void getMobIDToShare() {
		if (mobIdCache.containsKey(newsID)) {
			mobID = String.valueOf(mobIdCache.get(newsID));
			if (!TextUtils.isEmpty(mobID)) {
				share();
				return;
			}
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("newsID", newsID);
		MobLink.getMobID(params, CommonUtils.NEWS_PATH, CommonUtils.NEWS_SOURCE, new ActionListener() {
			public void onResult(HashMap<String, Object> params) {
				if (params != null && params.containsKey("mobID")) {
					mobID = String.valueOf(params.get("mobID"));
					mobIdCache.put(newsID, mobID);
					Log.i("Get mobID success ==>> ", mobID);
				} else {
					Toast.makeText(NewsDetailActivity.this, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
				}
				share();
			}

			public void onError(Throwable t) {
				if (t != null) {
					Toast.makeText(NewsDetailActivity.this, "error = " + t.getMessage(), Toast.LENGTH_SHORT).show();
				}
				share();
			}
		});
	}

	private void share() {
		String shareUrl = CommonUtils.SHARE_URL + CommonUtils.NEWS_PATH + "/" + newsID;
		if (!TextUtils.isEmpty(mobID)) {
			shareUrl += "/?mobid=" + mobID;
		}
		String title = tvNewsTitle.getText().toString();
		String imgPath = CommonUtils.copyImgToSD(this, newsShareIcon, "news_" + newsID);
		CommonUtils.showShare(this, title, title, shareUrl, imgPath);
	}
}
