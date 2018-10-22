package com.mob.moblink.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.QRcodeUtils;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.UIHandler;

public class QRcodeExampleActivity extends BaseActivity {
	private ImageView ivQrcode;
	private View llContent;
	private String title = null;
	private String text = null;
	private String url = null;
	private String imgPath = null;

	private String capturePath = null;
	private boolean captureIng = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Intent intent = getIntent();
		if (intent != null) {
			title = intent.getStringExtra("title");
			text = intent.getStringExtra("text");
			url = intent.getStringExtra("url");
			imgPath = intent.getStringExtra("imgPath");
		}

		setContentView(R.layout.activity_qrcode_detail);
		llContent = findViewById(R.id.llContent);
		ivQrcode = (ImageView) findViewById(R.id.ivQrcode);

		View btnShare = findViewById(R.id.tv_share);
		btnShare.setVisibility(View.GONE);
		Bitmap bitmap = null;
		try {
			bitmap = QRcodeUtils.encodeAsBitmap(url, BarcodeFormat.QR_CODE, 800, 900);
			ivQrcode.setImageBitmap(bitmap);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (bitmap == null) {
			Toast.makeText(getApplicationContext(), R.string.txt_moblink_share_qrcode_failed_1, Toast.LENGTH_SHORT).show();
			return;
		}
		btnShare.setVisibility(View.VISIBLE);
		btnShare.setOnClickListener(this);
		captureView(true);
	}

	private void captureView(final boolean isInit) {
		if (!TextUtils.isEmpty(capturePath)) {
			CommonUtils.showShareReal(this, getString(R.string.app_name),
					getString(R.string.txt_moblink_share_qrcode), null, capturePath);
			return;
		}
		if (!isInit && captureIng) {
			Toast.makeText(getApplicationContext(), R.string.txt_moblink_share_qrcode_toast, Toast.LENGTH_SHORT).show();
			return;
		}
		captureIng = true;
		new Thread() {
			public void run() {
				if (isInit) { //第一次初始化，则延迟200秒，保证llContent.getHeight()不会为空
					try {
						Thread.sleep(200);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
				try {
					Bitmap capture = BitmapHelper.captureView(llContent, llContent.getWidth(), llContent.getHeight());
					capturePath = BitmapHelper.saveBitmap(getApplicationContext(), capture);
				} catch (Throwable t) {
					t.printStackTrace();
				}
				captureIng = false;
				if (!isInit) {
					UIHandler.sendEmptyMessage(0, new Handler.Callback() {
						public boolean handleMessage(Message msg) {
							if (TextUtils.isEmpty(capturePath)) {
								Toast.makeText(getApplicationContext(), R.string.txt_moblink_share_qrcode_failed_2, Toast.LENGTH_SHORT).show();
							} else {
								CommonUtils.showShareReal(QRcodeExampleActivity.this, getString(R.string.app_name),
										getString(R.string.txt_moblink_share_qrcode), null, capturePath);
							}
							return false;
						}
					});
				}
			}
		}.start();
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_share: {
				//分享
				v.setEnabled(false);
				captureView(false);
				v.setEnabled(true);
			} break;
			default: {
				super.onClick(v);
			} break;
		}
	}
}
