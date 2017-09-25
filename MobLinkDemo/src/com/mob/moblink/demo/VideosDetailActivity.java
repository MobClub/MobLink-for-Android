package com.mob.moblink.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.Scene;
import com.mob.moblink.SceneRestorable;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;

public class VideosDetailActivity extends BaseActivity implements SceneRestorable {
	private static final String TAG = "VideosDetailActivity";
	private TextView tvShare;
	private TextView tvTitle;
	private WebView wvPlayVideo;
	private View videoSug01;
	private View videoSug02;
	private View videoSug03;
	private View videoSug04;

	private int videoIcon;//用于视频分享时的图片
	private int videoID;
	private String mobID;
	private HashMap<Integer, String> mobIdCache; //mobID缓存

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_videos_detail);
		tvShare = (TextView) findViewById(R.id.tv_share);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		wvPlayVideo = (WebView) findViewById(R.id.videoView);
		videoSug01 = findViewById(R.id.rl_video_sug01);
		videoSug02 = findViewById(R.id.rl_video_sug02);
		videoSug03 = findViewById(R.id.rl_video_sug03);
		videoSug04 = findViewById(R.id.rl_video_sug04);

		tvShare.setOnClickListener(this);
		videoSug01.setOnClickListener(this);
		videoSug02.setOnClickListener(this);
		videoSug03.setOnClickListener(this);
		videoSug04.setOnClickListener(this);

		wvPlayVideo.getSettings().setJavaScriptEnabled(true); // 开启Javascript支持
		wvPlayVideo.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		wvPlayVideo.getSettings().setLoadsImagesAutomatically(true);// 设置可以自动加载图片
		wvPlayVideo.getSettings().setAppCacheEnabled(true);// 应用可以有缓存
		wvPlayVideo.getSettings().setDomStorageEnabled(true);// 设置可以使用localStorage
		wvPlayVideo.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
		wvPlayVideo.getSettings().setAppCacheMaxSize(10 * 1024 * 1024);// 缓存最多可以有10M
		wvPlayVideo.getSettings().setAllowFileAccess(true);// 可以读取文件缓存(manifest生效)
		wvPlayVideo.getSettings().setPluginState(WebSettings.PluginState.ON);
		wvPlayVideo.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// 加速WebView加载的方法
		wvPlayVideo.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH); // 提高渲染的优先级
		wvPlayVideo.setWebChromeClient(new WebChromeClient());
		wvPlayVideo.setBackgroundColor(0x000000);

		mobIdCache = new HashMap<Integer, String>();

		if (getIntent() != null) {
			videoID = getIntent().getIntExtra("position", 0);
			setVideoDetail();
		}
	}

	protected void onPause() {
		super.onPause();
		try {
			wvPlayVideo.getClass().getMethod("onPause").invoke(wvPlayVideo, (Object[]) null);
		} catch (Exception e) {
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_share: {
				//分享
				getMobIDToShare();
			} break;
			case R.id.rl_video_sug01: {
				videoID = 100;
				setVideoDetail();
			} break;
			case R.id.rl_video_sug02: {
				videoID = 101;
				setVideoDetail();
			} break;
			case R.id.rl_video_sug03: {
				videoID = 102;
				setVideoDetail();
			} break;
			case R.id.rl_video_sug04: {
				videoID = 103;
				setVideoDetail();
			} break;
			default: {
				super.onClick(v);
			} break;
		}
	}

	private void setVideoDetail() {
		if (videoID < 100) {
			HashMap<String, Object> video = CommonUtils.getVideosData(this).get(videoID);
			String videoName = (String) video.get("videoName");
			String videoUrl = (String) video.get("videoUrl");
			videoIcon =  (Integer) video.get("videoIcon");
			tvTitle.setText(videoName);
			wvPlayVideo.loadUrl(videoUrl);
		} else if (videoID == 100) {
			videoIcon = R.drawable.demo_video_sug01;
			tvTitle.setText(R.string.video_sug01_name);
			wvPlayVideo.loadUrl(getString(R.string.video_sug01_play_url));
			videoSug01.setVisibility(View.GONE);
			videoSug02.setVisibility(View.VISIBLE);
			videoSug03.setVisibility(View.VISIBLE);
			videoSug04.setVisibility(View.VISIBLE);
		} else if (videoID == 101) {
			videoIcon = R.drawable.demo_video_sug02;
			tvTitle.setText(R.string.video_sug02_name);
			wvPlayVideo.loadUrl(getString(R.string.video_sug02_play_url));
			videoSug01.setVisibility(View.VISIBLE);
			videoSug02.setVisibility(View.GONE);
			videoSug03.setVisibility(View.VISIBLE);
			videoSug04.setVisibility(View.VISIBLE);
		} else if (videoID == 102) {
			videoIcon = R.drawable.demo_video_sug03;
			tvTitle.setText(R.string.video_sug03_name);
			wvPlayVideo.loadUrl(getString(R.string.video_sug03_play_url));
			videoSug01.setVisibility(View.VISIBLE);
			videoSug02.setVisibility(View.VISIBLE);
			videoSug03.setVisibility(View.GONE);
			videoSug04.setVisibility(View.VISIBLE);
		} else if (videoID == 103) {
			videoIcon = R.drawable.demo_video_sug04;
			tvTitle.setText(R.string.video_sug04_name);
			wvPlayVideo.loadUrl(getString(R.string.video_sug04_play_url));
			videoSug01.setVisibility(View.VISIBLE);
			videoSug02.setVisibility(View.VISIBLE);
			videoSug03.setVisibility(View.VISIBLE);
			videoSug04.setVisibility(View.GONE);
		}
	}

	private void getMobIDToShare() {
		if (mobIdCache.containsKey(videoID)) {
			mobID = String.valueOf(mobIdCache.get(videoID));
			if (!TextUtils.isEmpty(mobID)) {
				share();
				return;
			}
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("videoID", videoID);
		Scene s = new Scene();
		s.path = CommonUtils.VIDEO_PATH;
		s.source = CommonUtils.VIDEO_SOURCE;
		s.params = params;
		MobLink.getMobID(s, new ActionListener<String>() {
			public void onResult(String mobID) {
				if (mobID != null) {
					VideosDetailActivity.this.mobID = mobID;
					mobIdCache.put(videoID, mobID);
				} else {
					Toast.makeText(VideosDetailActivity.this, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
				}
				share();
			}

			public void onError(Throwable t) {
				if (t != null) {
					Toast.makeText(VideosDetailActivity.this, "error = " + t.getMessage(), Toast.LENGTH_SHORT).show();
				}
				share();
			}
		});
	}

	private void share() {
		String shareUrl = CommonUtils.SHARE_URL + CommonUtils.VIDEO_PATH + "/" + videoID;
		if (!TextUtils.isEmpty(mobID)) {
			shareUrl += "?mobid=" + mobID;
		}
		String title = tvTitle.getText().toString();
		String text = tvTitle.getText().toString();
		String imgPath = CommonUtils.copyImgToSD(this, videoIcon, "video_" + videoID);
		CommonUtils.showShare(this, title, text, shareUrl, imgPath);
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
			if (null != scene.params) {
				String value = null;
				if (scene.params.containsKey("videoID")) {
					value = String.valueOf(scene.params.get("videoID"));
				} else if (scene.params.containsKey("id")) {
					value = String.valueOf(scene.params.get("id"));
				}
				if (!TextUtils.isEmpty(value)) {
					videoID = Integer.parseInt(String.valueOf(value));
					setVideoDetail();
				}
			}
		}
	}
}
