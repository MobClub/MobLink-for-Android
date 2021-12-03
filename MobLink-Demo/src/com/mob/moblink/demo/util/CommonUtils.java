package com.mob.moblink.demo.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mob.moblink.MobLink;
import com.mob.moblink.demo.ActivityA;
import com.mob.moblink.demo.ActivityB;
import com.mob.moblink.demo.ActivityC;
import com.mob.moblink.demo.ActivityD;
import com.mob.moblink.demo.InviteDetailActivity;
import com.mob.moblink.demo.NewsDetailActivity;
import com.mob.moblink.demo.QRcodeExampleActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.ShoppingActivity;
import com.mob.moblink.demo.TicketDetailActivity;
import com.mob.moblink.demo.VideosDetailActivity;
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
	public static final String VIDEO_PATH = "/scene/video";
	public static final String SHOPPING_PATH = "/scene/shopping";
	public static final String INVITE_PATH = "/params/invite";
	public static final String TICKET_PATH = "/params/ticket";

	public static final Map<String, Class<? extends Activity>> PATH_SERVER_MAP = new HashMap<String, Class<? extends Activity>>();
	public static final Map<String, Class<? extends Activity>> PATH_MAP_LOCAL = new HashMap<String, Class<? extends Activity>>();
	static {
		PATH_MAP_LOCAL.put(NEWS_PATH, NewsDetailActivity.class);
		PATH_MAP_LOCAL.put(VIDEO_PATH, VideosDetailActivity.class);
		PATH_MAP_LOCAL.put(SHOPPING_PATH, ShoppingActivity.class);
		PATH_MAP_LOCAL.put(INVITE_PATH, InviteDetailActivity.class);
		PATH_MAP_LOCAL.put(TICKET_PATH, TicketDetailActivity.class);

		PATH_SERVER_MAP.put("/demo/a", ActivityA.class);
		PATH_SERVER_MAP.put("/demo/b", ActivityB.class);
		PATH_SERVER_MAP.put("/demo/c", ActivityC.class);
		PATH_SERVER_MAP.put("/demo/d", ActivityD.class);
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
	 * 获取视频列表数据
	 * @param context
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getVideosData(Context context) {
		int[] videoIcon = {R.drawable.demo_video01, R.drawable.demo_video02, R.drawable.demo_video03,
				R.drawable.demo_video04, R.drawable.demo_video05, R.drawable.demo_video06,
				R.drawable.demo_video07, R.drawable.demo_video08, R.drawable.demo_video09,};
		String[] videoName = context.getResources().getStringArray(R.array.video_name);
		String[] videoUrl = context.getResources().getStringArray(R.array.video_url);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < videoIcon.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("videoIcon", videoIcon[i]);
			item.put("videoName", videoName[i]);
			item.put("videoUrl", videoUrl[i]);
			list.add(item);
		}
		return  list;
	}

	/**
	 * 获取商品列表数据
	 * @param context
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getGoodsData(Context context) {
		int[] goodsIcon = {R.drawable.demo_ds01, R.drawable.demo_ds02, R.drawable.demo_ds03,
				R.drawable.demo_ds04, R.drawable.demo_ds05, R.drawable.demo_ds06,
				R.drawable.demo_ds07, R.drawable.demo_ds08, R.drawable.demo_ds09,};
		int[] goodsBigIcon = {R.drawable.demo_ds01_big, R.drawable.demo_ds02_big, R.drawable.demo_ds03_big,
				R.drawable.demo_ds04_big, R.drawable.demo_ds05_big, R.drawable.demo_ds06_big,
				R.drawable.demo_ds07_big, R.drawable.demo_ds08_big, R.drawable.demo_ds09_big,};
		String[] goodsName = context.getResources().getStringArray(R.array.goods_name);
		String[] goodsPrice = context.getResources().getStringArray(R.array.goods_price);
		String[] goodsSales = context.getResources().getStringArray(R.array.goods_sales);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < goodsIcon.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("goodsIcon", goodsIcon[i]);
			item.put("goodsBigIcon", goodsBigIcon[i]);
			item.put("goodsName", goodsName[i]);
			item.put("goodsPrice", goodsPrice[i]);
			item.put("goodsSales", goodsSales[i]);
			list.add(item);
		}
		return  list;
	}

	/**
	 * 获取飞机票列表数据
	 * @param context
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getTicketsDate(Context context) {
		String[] fromTime = context.getResources().getStringArray(R.array.fly_from_time);
		String[] toTime = context.getResources().getStringArray(R.array.fly_to_time);
		String[] ticketPrice = context.getResources().getStringArray(R.array.ticket_price);
		String[] ticketDiscount = context.getResources().getStringArray(R.array.ticket_discount);
		String[] planeName = context.getResources().getStringArray(R.array.plane_name);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < fromTime.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("fromTime", fromTime[i]);
			item.put("ToTime", toTime[i]);
			item.put("ticketPrice", ticketPrice[i]);
			item.put("ticketDiscount", ticketDiscount[i]);
			item.put("planeName", planeName[i]);
			list.add(item);
		}
		return  list;
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
				if ("SinaWeibo".endsWith(platform.getName())
						|| "Twitter".endsWith(platform.getName())) {
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

	public static String getShareUrl() {
		String shareUrl;
		// 3.0.0短链版本
		if (DEBUGGABLE) {
			shareUrl = "http://f.moblink.mob.com/demoPro";
		} else {
			shareUrl = "http://f.moblink.mob.com/demoPro";
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
}
