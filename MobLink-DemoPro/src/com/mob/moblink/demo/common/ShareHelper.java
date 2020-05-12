package com.mob.moblink.demo.common;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mob.moblink.demo.QRcodeExampleActivity;
import com.mob.moblink.demo.R;
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
					paramsToShare.setUrl(null);
					paramsToShare.setTitle(null);
					paramsToShare.setTitleUrl(null);
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
