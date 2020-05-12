package com.mob.moblink.demo.restore.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.Scene;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.ShareableActivity;
import com.mob.moblink.demo.restore.presenter.RestorePresenter;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.SPHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsDetailActivity extends ShareableActivity implements IRestoreView {
	private static final String TAG = "NewsDetailActivity";
	private TextView tvShare;
	private View llNewsDetail ;
	private TextView tvNewsTime;
	private TextView tvNewsTitle;
	private TextView tvNewsDetail;
	private View newsSug01;
	private View newsSug02;

	private int newsID;
	private String mobID;
	private int newsShareIcon;
	private HashMap<Integer, String> mobIdCache; //mobID缓存
	private RestorePresenter restorePresenter;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_news_detail;
	}

	@Override
	protected int getTitleId() {
		return R.string.today_news;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		tvShare = (TextView) findViewById(R.id.tv_share);
		llNewsDetail = findViewById(R.id.ll_news_detail);
		tvNewsTitle = (TextView) llNewsDetail.findViewById(R.id.tv_news_title);
		tvNewsDetail = (TextView) llNewsDetail.findViewById(R.id.tv_news_detail);
		tvNewsTime = (TextView) llNewsDetail.findViewById(R.id.tv_news_time);
		newsSug01 = findViewById(R.id.rl_news_sug01);
		newsSug02 = findViewById(R.id.rl_news_sug02);

		tvShare.setOnClickListener(this);
		newsSug01.setOnClickListener(this);
		newsSug02.setOnClickListener(this);

		mobIdCache = new HashMap<Integer, String>();

		if (getIntent() != null) {
			newsID = getIntent().getIntExtra("position", newsID);
		}
		setNewsDetail();

		restorePresenter = new RestorePresenter(this);
	}

	@Override
	protected void onRightEvent() {
		//分享
		getMobIDToShare();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//			case R.id.tv_share: {
//				//分享
//				getMobIDToShare();
//			} break;
			case R.id.rl_news_sug01: {
				if (newsID == 0) {
					newsID = 1;
				} else {
					newsID = 0;
				}
				setNewsDetail();
			} break;
			case R.id.rl_news_sug02: {
				if (newsID == 2) {
					newsID = 1;
				} else {
					newsID = 2;
				}
				setNewsDetail();
			} break;
			default: {
				super.onClick(v);
			} break;
		}
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

	@Override
	public void onMobIdGot(String mobId) {
		if (mobId != null) {
			mobID = mobId;
			mobIdCache.put(newsID, mobId);
		} else {
			Toast.makeText(NewsDetailActivity.this, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
		}
		share();
	}

	@Override
	public void onMobIdError(Throwable t) {
		if (t != null) {
			Toast.makeText(NewsDetailActivity.this, "error = " + t.getMessage(), Toast.LENGTH_SHORT).show();
		}
		share();
	}

	private void setNewsDetail() {
		ArrayList<HashMap<String, Object>> data = CommonUtils.getNewsData(this);

		HashMap<String, Object> news =  data.get(newsID);
		tvNewsTitle.setText((String) news.get("title"));
		tvNewsDetail.setText((String) news.get("detail"));
		tvNewsTime.setText((String) news.get("time"));
		newsShareIcon = (Integer) news.get("img");

		ImageView icon = (ImageView) findViewById(R.id.iv_news_icon);
		TextView title = (TextView) findViewById(R.id.iv_news_title1);
		TextView comm = (TextView) findViewById(R.id.iv_news_comm1);

		int otherNewsId = 0;
		if (newsID == 0) {
			otherNewsId = 1;
		}
		news =  data.get(otherNewsId);
		icon.setImageResource((Integer) news.get("img"));
		title.setText((String) news.get("title"));
		comm.setText((String) news.get("time"));

		icon = (ImageView) findViewById(R.id.iv_news_icon2);
		title = (TextView) findViewById(R.id.iv_news_title2);
		comm = (TextView) findViewById(R.id.iv_news_comm2);

		otherNewsId = 2;
		if (newsID == 2) {
			otherNewsId = 1;
		}
		news =  data.get(otherNewsId);
		icon.setImageResource((Integer) news.get("img"));
		title.setText((String) news.get("title"));
		comm.setText((String) news.get("time"));
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
		params.put("id", SPHelper.getDemoUserId());
		params.put("scene", CommonUtils.SCENE_NEWS);
		restorePresenter.getMobId(CommonUtils.NEWS_PATH, params);
	}

	private void share() {
		String shareUrl = CommonUtils.getShareUrl() + CommonUtils.NEWS_PATH + "?id=" + newsID;
		if (!TextUtils.isEmpty(mobID)) {
			shareUrl += "&mobid=" + mobID;
		}
		String title = tvNewsTitle.getText().toString();
		String imgPath = CommonUtils.copyImgToSD(this, newsShareIcon, "news_" + newsID);
		restorePresenter.share(this, title, title, shareUrl, imgPath);
	}
}
