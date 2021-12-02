package com.mob.moblink.demo.common;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.demo.QRcodeExampleActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.ShareOrVCodeDialog;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class ShareHelper {

	/**
	 * 分享
	 *
	 * @param context
	 * @param title
	 * @param text
	 * @param url
	 * @param imgPath
	 */
	public static void showShare(final Context context, final String title, final String text, final String url, final String mobID, final String imgPath) {
		new ShareOrVCodeDialog(context).setOnItemClickListener(new ShareOrVCodeDialog.OnItemClickListener() {
			public void onItemClick(int position) {
				if (position == 1) {
					//分享
					showShareReal(context, title, text, url,mobID, imgPath);
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

	public static void showShareReal(final Context context, final String title, final String text, final String url, final String mobID, final String imgPath) {
		OnekeyShare oks = new OnekeyShare();
		oks.setTitle(title);
		oks.setText(text);
		oks.setUrl(url);
		oks.setTitleUrl(url);
		oks.setImagePath(imgPath);
		Bitmap logo = BitmapFactory.decodeResource(context.getResources(),R.drawable.ssdk_oks_classic_duplicate);
		oks.setCustomerLogo(logo, context.getString(R.string.copy_url), new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (android.os.Build.VERSION.SDK_INT >= 11) {
					ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clipData = ClipData.newPlainText("", url);
					clipboardManager.setPrimaryClip(clipData);
				} else{
					android.text.ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
					clipboardManager.setText(url);
				}
				Toast.makeText(context,R.string.copy_url_to_clipboard,Toast.LENGTH_LONG).show();
			}
		});
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
				if ("SinaWeibo".endsWith(platform.getName())
						|| "Twitter".endsWith(platform.getName())) {
					String text = paramsToShare.getText() + paramsToShare.getUrl();
					paramsToShare.setText(text);
					paramsToShare.setUrl(null);
					paramsToShare.setTitle(null);
					paramsToShare.setTitleUrl(null);
				} else if ("ShortMessage".endsWith(platform.getName())) {
					paramsToShare.setImagePath(null);
					String url =  context.getString(R.string.share_message_content,CommonUtils.getAppLinkShareUrl()+"/"+mobID);
					paramsToShare.setText(url);

				}else if ("QQ".endsWith(platform.getName())){
					//QQ分享bu
					if(!TextUtils.isEmpty(imgPath) && (TextUtils.isEmpty(title) ||TextUtils.isEmpty(text)||TextUtils.isEmpty(url))){
						paramsToShare.setTitle(null);
						paramsToShare.setText(null);
						paramsToShare.setUrl(null);
						paramsToShare.setTitleUrl(null);
					}
				}
			}
		});
		oks.show(context);
	}

	/**
	 * 获取mobid后才能分享的对话框
	 *
	 * @param context
	 * @return
	 */
	public static Dialog noMobIdWarning(Context context) {
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
}
