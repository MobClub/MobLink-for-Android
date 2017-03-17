package com.mob.moblink.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.tools.utils.BitmapHelper;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class VideosDetailActivity extends Activity implements View.OnClickListener {

	private ImageView ivBack;
	private TextView tvShare;
	private TextView tvTitle;
	private WebView wvPlayVideo;
	private View videoSug01;
	private View videoSug02;
	private View videoSug03;
	private View videoSug04;

	private Dialog dialog;

	private int videoIcon;//用于视频分享时的图片
	private int videoID;
	private String mobID;
	private HashMap<Integer, String> mobIdCache; //mobID缓存

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_videos_detail);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvShare = (TextView) findViewById(R.id.tv_share);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		wvPlayVideo = (WebView) findViewById(R.id.videoView);
		videoSug01 = findViewById(R.id.rl_video_sug01);
		videoSug02 = findViewById(R.id.rl_video_sug02);
		videoSug03 = findViewById(R.id.rl_video_sug03);
		videoSug04 = findViewById(R.id.rl_video_sug04);

		ivBack.setOnClickListener(this);
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
	}

	protected void onResume() {
		super.onResume();
		if (getIntent() != null) {
			videoID = getIntent().getIntExtra("position", 0);
			setVideoDetail();
		}
		MobLink.initSDK(this, CommonUtils.APPKEY);
		MobLink.setIntentHandler(getIntent(), new ActionListener() {
			public void onResult(final HashMap<String, Object> res) {
				runOnUiThread(new Runnable() {
					public void run() {
						String sourceStr = "";
						String paramStr = "";
						if (res != null) {
							if (res.get("source") != null) {
								sourceStr = String.valueOf(res.get("source"));
							}
							if (res.get("params") != null) {
								HashMap<String, Object> params = (HashMap<String, Object>) res.get("params");
								for (Map.Entry<String, Object> entry : params.entrySet()) {
									if ("videoID".equals(entry.getKey()) || "id".equals(entry.getKey())) {
										videoID = Integer.parseInt(String.valueOf(entry.getValue()));
										setVideoDetail();
									}
									paramStr += (entry.getKey() + " : " + entry.getValue() + "\r\n");
								}
							}
						}

						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						dialog = CommonUtils.getDialog(VideosDetailActivity.this, CommonUtils.VIDEO_PATH, sourceStr, paramStr);
						dialog.show();
					}
				});
			}

			public void onError(Throwable t) {
				if (t != null) {
					Toast.makeText(VideosDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		setIntent(null);
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
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
			case R.id.iv_back: {
				Intent i = new Intent(this, VideosActivity.class);
				startActivity(i);
			} break;
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
			default:
			break;
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
		MobLink.getMobID(params, CommonUtils.VIDEO_PATH, CommonUtils.VIDEO_SOURCE, new ActionListener() {
			public void onResult(HashMap<String, Object> params) {
				if (params != null && params.containsKey("mobID")) {
					mobID = String.valueOf(params.get("mobID"));
					mobIdCache.put(videoID, mobID);
					Log.i("Get mobID success ==>> ", mobID);
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
			shareUrl += "/?mobid=" + mobID;
		}
		String title = tvTitle.getText().toString();
		String text = tvTitle.getText().toString();
		String imgPath = CommonUtils.copyImgToSD(this, videoIcon, "video_" + videoID);
		CommonUtils.showShare(this, title, text, shareUrl, imgPath);
	}

}
