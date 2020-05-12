package com.mob.moblink.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.Scene;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.HashMap;

public class NewsDetailActivity extends ShareableActivity {
	private static final String TAG = "NewsDetailActivity";
	private TextView tvShare;
	private View llNewsDetail ;
	private TextView tvNewsTime;
	private TextView tvNewsTitle;
	private TextView tvNewsDetail;
	private View newsSug01;
	private View newsSug02;
	private View newsSug03;

	private int newsID;
	private String mobID;
	private int newsShareIcon;
	private HashMap<Integer, String> mobIdCache; //mobID缓存

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		tvShare = (TextView) findViewById(R.id.tv_share);
		llNewsDetail = findViewById(R.id.ll_news_detail);
		tvNewsTitle = (TextView) llNewsDetail.findViewById(R.id.tv_news_title);
		tvNewsDetail = (TextView) llNewsDetail.findViewById(R.id.tv_news_detail);
		tvNewsTime = (TextView) llNewsDetail.findViewById(R.id.tv_news_time);
		newsSug01 = findViewById(R.id.rl_news_sug01);
		newsSug02 = findViewById(R.id.rl_news_sug02);
		newsSug03 = findViewById(R.id.rl_news_sug03);

		tvShare.setOnClickListener(this);
		newsSug01.setOnClickListener(this);
		newsSug02.setOnClickListener(this);
		newsSug03.setOnClickListener(this);

		mobIdCache = new HashMap<Integer, String>();

		if (getIntent() != null) {
			newsID = getIntent().getIntExtra("position", newsID);
		}
		setNewsDetail();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
			default: {
				super.onClick(v);
			} break;
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
		Scene s = new Scene();
		s.setPath(CommonUtils.NEWS_PATH);
		s.setParams(params);
		MobLink.getMobID(s, new ActionListener<String>() {
			public void onResult(String mobID) {
				if (mobID != null) {
					NewsDetailActivity.this.mobID = mobID;
					mobIdCache.put(newsID, mobID);
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
		String shareUrl = CommonUtils.getShareUrl() + CommonUtils.NEWS_PATH + "/" + newsID;
		if (!TextUtils.isEmpty(mobID)) {
			shareUrl += "?mobid=" + mobID;
		}
		String title = tvNewsTitle.getText().toString();
		String imgPath = CommonUtils.copyImgToSD(this, newsShareIcon, "news_" + newsID);
		CommonUtils.showShare(this, title, title, shareUrl, imgPath);
	}

	@Override
	public void onReturnSceneData(Scene scene) {
		super.onReturnSceneData(scene);
		if (scene != null) {
			if (null != scene.getParams()) {
				String value = null;
				if (scene.getParams().containsKey("newsID")) {
					value = String.valueOf(scene.getParams().get("newsID"));
				} else if (scene.getParams().containsKey("id")) {
					value = String.valueOf(scene.getParams().get("id"));
				}
				if (!TextUtils.isEmpty(value)) {
					newsID = Integer.parseInt(String.valueOf(value));
				}
			}
		}
	}
}
