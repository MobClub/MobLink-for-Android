package com.mob.moblink.demo.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.security.NetworkSecurityPolicy;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mob.moblink.MobLink;
import com.mob.moblink.demo.QRcodeExampleActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.invite.LocalInviteActivity;
import com.mob.moblink.demo.invite.ShareInviteActivity;
import com.mob.moblink.demo.profile.FriendActivity;
import com.mob.moblink.demo.restore.game.GameActivity;
import com.mob.moblink.demo.restore.novel.NovelReadActivity;
import com.mob.moblink.demo.restore.view.NewsDetailActivity;
import com.mob.moblink.demo.splash.SplashActivity;
import com.mob.tools.utils.ResHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;

public class CommonUtils {

	/**
	 * 标识, 连接是不是测试服
	 */
	public static final boolean DEBUGGABLE = MobLink.DEBUGGABLE;

	public static final String[] MAIN_PATH_ARR = {"/demo/a", "/demo/b", "/demo/c", "/demo/d"};
	public static final String NEWS_PATH = "/scene/news";
	public static final String NOVEL_PATH = "/scene/novel";
	public static final String GAME_PATH = "/scene/game";
	public static final String MATCH_PATH = "/relationship";
	public static final String WAKEUP_PATH = "/demo";
	public static final String LOCAL_INVITE_PATH = "/invite/local";
	public static final String SHARE_INVITE_PATH = "/invite/share";

	public static final int SCENE_NEWS = 2001;
	public static final int SCENE_NOVEL = 2002;
	public static final int SCENE_GAME = 2003;
	public static final int SCENE_LOCAL_INVITE = 2004;
	public static final int SCENE_SHARE_INVITE = 2005;
	public static final int SCENE_MATCH = 2006;

	public static final Map<String, Class<? extends Activity>> PATH_SERVER_MAP = new HashMap<String, Class<? extends Activity>>();
	public static final Map<String, Class<? extends Activity>> PATH_MAP_LOCAL = new HashMap<String, Class<? extends Activity>>();
	static {
		PATH_MAP_LOCAL.put(NEWS_PATH, NewsDetailActivity.class);
		PATH_MAP_LOCAL.put(NOVEL_PATH, NovelReadActivity.class);
		PATH_MAP_LOCAL.put(GAME_PATH, GameActivity.class);
		PATH_MAP_LOCAL.put(MATCH_PATH, FriendActivity.class);
		PATH_MAP_LOCAL.put(WAKEUP_PATH, SplashActivity.class);
		PATH_MAP_LOCAL.put(LOCAL_INVITE_PATH, LocalInviteActivity.class);
		PATH_MAP_LOCAL.put(SHARE_INVITE_PATH, ShareInviteActivity.class);
	}
	
	/**
	 * 创建一个自定义dialog，场景还原的参数展示dialog
	 * @param context
	 * @param path
	 * @param params
	 * @return
	 */
	public static Dialog getDialog(Context context, String path, String params) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.dialog);
		if (!TextUtils.isEmpty(path)) {
			((TextView) dialog.findViewById(R.id.tv_dialog_path)).append("\r\n" + path);
		}
		if (!TextUtils.isEmpty(params)) {
			((TextView) dialog.findViewById(R.id.tv_dialog_param)).append("\r\n" + params);
		}
		dialog.findViewById(R.id.tv_dialog_close).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	/**
	 * 是否恢复场景的对话框
	 * @param context
	 * @param runnable
	 * @return
	 */
	public static Dialog getRestoreSceneDialog(Context context, final Runnable runnable) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.alert_dialog);
		dialog.findViewById(R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.findViewById(R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (runnable != null) {
					runnable.run();
				}
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	/**
	 * 获取mobid后才能分享的对话框
	 * @param context
	 * @return
	 */
	public static Dialog getMobIdDialog(Context context) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.alert_dialog);
		dialog.findViewById(R.id.dialog_btn_no).setVisibility(View.GONE);
		dialog.findViewById(R.id.dialog_v_line).setVisibility(View.GONE);
		TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_dialog_title);
		tvTitle.setText(R.string.please_get_mobid);
		TextView tvYes = (TextView) dialog.findViewById(R.id.dialog_btn_yes);
		tvYes.setText(R.string.close);
		tvYes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}
	
	/**
	 * 获取新闻列表数据
	 * @param context
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getNewsData(Context context) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		int[] newsImg = {R.drawable.demo_news01, R.drawable.demo_news02, R.drawable.demo_news03,
				R.drawable.demo_news04, R.drawable.demo_news05, R.drawable.demo_news06, R.drawable.demo_news07};
		String[] newsTitle = context.getResources().getStringArray(R.array.news_titles);
		String[] newsCommments = context.getResources().getStringArray(R.array.news_commments);
		String[] newsDetail = context.getResources().getStringArray(R.array.news_details);
		String[] newsTime = context.getResources().getStringArray(R.array.news_time);
		for (int i = 0; i < newsImg.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("img", newsImg[i]);
			item.put("title", newsTitle[i]);
			item.put("comm", newsCommments[i]);
			if (i == 0) {
				item.put("top", true);
				item.put("opic", true);
			} else {
				item.put("top", false);
				item.put("opic", false);
			}
			item.put("time", newsTime[i]);
			item.put("detail", newsDetail[i]);
			list.add(item);
		}		
		return list;
	}


	/**
	 * 复制res中的图片到sdcard中
	 * @param context
	 * @param imgID
	 * @param imgName
	 * @return
	 */
	public static String copyImgToSD(Context context, int imgID, String imgName) {
		String imgPaht = "";
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgID);
		if (bitmap != null && !bitmap.isRecycled()) {
			String path = ResHelper.getImageCachePath(context);
			if (TextUtils.isEmpty(imgName)) {
				imgName = String.valueOf(System.currentTimeMillis());
			}
			File file = new File(path, imgName + ".jpg");
			if (file.exists()) {
				return file.getAbsolutePath();
			}
			try {
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
				fos.flush();
				fos.close();
				imgPaht = file.getAbsolutePath();
			} catch (Throwable t) {
			}
		}
		return imgPaht;
	}

	/**
	 * 分享
	 * @param context
	 * @param title
	 * @param text
	 * @param url
	 * @param imgPath
	 */
	public static void showShare(final Context context, final String title, final String text, final String url, final String imgPath) {
		new ShareOrVCodeDialog(context).setOnItemClickListener(new ShareOrVCodeDialog.OnItemClickListener() {
			public void onItemClick(int position) {
				if (position == 1) {
					//分享
					showShareReal(context, title, text, url, imgPath);
				} else if (position == 2) {
					Intent intent = new Intent(context, QRcodeExampleActivity.class);
					intent.putExtra("title", title);
					intent.putExtra("text", text);
					intent.putExtra("url", url);
					intent.putExtra("imgPath", imgPath);
					context.startActivity(intent);
				}
			}
		}).show();
	}

	public static void showShareReal(Context context, String title, String text, String url, String imgPath) {
		OnekeyShare oks = new OnekeyShare();
		oks.setTitle(title);
		oks.setText(text);
		oks.setUrl(url);
		oks.setTitleUrl(url);
		oks.setImagePath(imgPath);
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
				if ("SinaWeibo".endsWith(platform.getName())) {
					String text = paramsToShare.getText() + paramsToShare.getUrl();
					paramsToShare.setText(text);
				} else if ("ShortMessage".endsWith(platform.getName())) {
					paramsToShare.setImagePath(null);
					String value = paramsToShare.getText();
					value += "\n";
					String url = paramsToShare.getUrl();
					value += url;
					paramsToShare.setText(value);

				}
			}
		});
		oks.show(context);
	}

	/**
	 * 分享到微信小程序
	 */
	public static void showShareWxMini(final Context context, final String mobId) {
		OnekeyShare oks = new OnekeyShare();
		oks.setPlatform(Wechat.NAME);
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
				Platform.ShareParams shareParams = paramsToShare;
				shareParams.setText("Test MiniProgram For MobLink");
				shareParams.setTitle("MiniProgram For MobLink");
				shareParams.setUrl("http://www.mob.com");
				shareParams.setShareType(Platform.SHARE_WXMINIPROGRAM);

				String value = context.getResources().getString(R.string.wx_share_custom_value);
				shareParams.setWxPath("pages/index/index?mobid=" + mobId + "&custom=" + value);
				String imgPath = CommonUtils.copyImgToSD(context, R.drawable.demo_share_wxmini, "moblink-wxmini");
				shareParams.setImagePath(imgPath);
			}
		});
		oks.show(context);
	}

	public static Bitmap getCircleAvatar(Context context, Bitmap avatar) {
		Bitmap bitmap = Bitmap.createBitmap(avatar.getWidth(), avatar.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setColor(Color.BLACK);
		canvas.drawCircle(avatar.getWidth() / 2, avatar.getHeight() / 2,
				avatar.getWidth() / 2 < avatar.getHeight() / 2 ? avatar.getWidth() / 2 : avatar.getHeight() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(avatar, 0, 0, paint);
		return bitmap;
	}

	public static String getShareUrl() {
		String shareUrl;
		// 3.0.0短链版本
		if (DEBUGGABLE) {
			shareUrl = "http://10.18.97.58:2122";
		} else {
			shareUrl = "http://f.moblink.mob.com/pro";
		}
		return shareUrl;
	}

	public static String getAppLinkShareUrl() {
		String shareUrl;
		// 3.0.0短链版本
		if (DEBUGGABLE) {
			shareUrl = "http://70r9.link.mob.com";
		} else {
			shareUrl = "http://z.t4m.cn";
		}
		return shareUrl;
	}

	public static String getDemoUrl() {
		String demoUrl;
		//3.0.2 demo改版接口
		if (DEBUGGABLE) {
			demoUrl = "http://172.25.49.63:8999";
		} else {
			demoUrl = "http://61.174.10.198:8999";
		}
		return demoUrl;
	}

	public static String checkHttpRequestUrl(String requestUrl) {
		try {
			if (!TextUtils.isEmpty(requestUrl) && Build.VERSION.SDK_INT >= 23 && !NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted()) {
				requestUrl = requestUrl.trim();
				if (requestUrl.startsWith("http://")) {
					Uri uri = Uri.parse(requestUrl.trim());
					if (uri != null) {
						String scheme = uri.getScheme();
						if (scheme != null && scheme.equals("http")) {
							String host = uri.getHost();
							String path = uri.getPath();
							if (host != null) {
								int port = uri.getPort();
								host = host + (port > 0 && port != 80 ? ":" + port : "");
								if (Build.VERSION.SDK_INT >= 24 && NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(host)) {
									return requestUrl;
								}
							}

							return "https://" + host + (path == null ? "" : path);
						}
					}
				}
			}
		} catch (Throwable t) {
			Log.d("CommonUtils", "checkHttpRequestUrl encountered problems", t);
		}

		return requestUrl;
	}
}
