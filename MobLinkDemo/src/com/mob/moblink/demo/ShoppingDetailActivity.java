package com.mob.moblink.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class ShoppingDetailActivity extends Activity implements View.OnClickListener{

	private ImageView ivBack;
	private TextView tvShare;
	private TextView tvTitle;
	private ImageView ivGoodsIcon;
	private TextView tvGoodsDetail;
	private TextView tvGoodsPrice;
	private View goodsSug01;
	private View goodsSug02;
	private View goodsSug03;

	private Dialog dialog;
	private int goodsID;
	private String mobID;
	private int goodsShareIcon;
	private HashMap<Integer, String> mobIdCache; //mobID缓存

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_shopping_detail);

		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvShare = (TextView) findViewById(R.id.tv_share);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivGoodsIcon = (ImageView) findViewById(R.id.iv_goods_icon);
		tvGoodsDetail = (TextView) findViewById(R.id.tv_goods_detail);
		tvGoodsPrice = (TextView) findViewById(R.id.tv_goods_price);
		goodsSug01 = findViewById(R.id.rl_goods_sug01);
		goodsSug02 = findViewById(R.id.rl_goods_sug02);
		goodsSug03 = findViewById(R.id.rl_goods_sug03);

		ivBack.setOnClickListener(this);
		tvShare.setOnClickListener(this);
		goodsSug01.setOnClickListener(this);
		goodsSug02.setOnClickListener(this);
		goodsSug03.setOnClickListener(this);

		mobIdCache = new HashMap<Integer, String>();
	}

	protected void onResume() {
		super.onResume();
		if (getIntent() != null) {
			goodsID = getIntent().getIntExtra("position", goodsID);
			setGoodsDetail();
		}
		MobLink.initSDK(this, CommonUtils.APPKEY);
		MobLink.setIntentHandler(getIntent(), new ActionListener() {
			public void onResult(final HashMap<String, Object> res) {
				runOnUiThread(new Runnable() {
					public void run() {
						String paramStr = "";
						String sourceStr = null;
						if (res != null) {
							if (res.get("source") != null) {
								sourceStr = String.valueOf(res.get("source"));
							}
							if (res.get("params") != null) {
								HashMap<String, Object> params = (HashMap<String, Object>) res.get("params");
								for (Map.Entry<String, Object> entry : params.entrySet()) {
									if ("goodsID".equals(entry.getKey()) || "id".equals(entry.getKey())) {
										goodsID = Integer.parseInt(String.valueOf(entry.getValue()));
										setGoodsDetail();
									}
									paramStr += (entry.getKey() + " : " + entry.getValue() + "\r\n");
								}
							}
						}

						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						dialog = CommonUtils.getDialog(ShoppingDetailActivity.this, CommonUtils.SHOPPING_PATH, sourceStr, paramStr);
						dialog.show();
					}
				});
			}
			public void onError(Throwable t) {
				if (t != null) {
					Toast.makeText(ShoppingDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
				Intent i = new Intent(this, ShoppingActivity.class);
				startActivity(i);
			} break;
			case R.id.tv_share: {
				//分享
				getMobIDToShare();
			} break;
			case R.id.rl_goods_sug01: {
				goodsID = 100;
				setGoodsDetail();
			} break;
			case  R.id.rl_goods_sug02: {
				goodsID = 101;
				setGoodsDetail();
			} break;
			case  R.id.rl_goods_sug03: {
				goodsID = 102;
				setGoodsDetail();
			} break;
			default:
			break;
		}
	}
	private void setGoodsDetail() {
		if (goodsID < 100) {
			HashMap<String, Object> goods = CommonUtils.getGoodsData(this).get(goodsID);
			String goodsName = (String) goods.get("goodsName");
			String goodsPrice = (String) goods.get("goodsPrice");
			Integer goodsBigIcon = (Integer) goods.get("goodsBigIcon");
			goodsShareIcon = (Integer) goods.get("goodsIcon");
			tvGoodsDetail.setText(goodsName);
			tvGoodsPrice.setText(goodsPrice);
			ivGoodsIcon.setImageResource(goodsBigIcon);
			goodsSug01.setVisibility(View.VISIBLE);
			goodsSug02.setVisibility(View.VISIBLE);
		} else if (goodsID == 100) {
			goodsShareIcon = R.drawable.demo_dsxg01;
			tvGoodsDetail.setText(R.string.goods_sug01_name);
			tvGoodsPrice.setText(R.string.goods_sug01_price);
			ivGoodsIcon.setImageResource(R.drawable.demo_dsxg01_big);
			goodsSug01.setVisibility(View.GONE);
			goodsSug02.setVisibility(View.VISIBLE);
			goodsSug03.setVisibility(View.VISIBLE);
		} else if (goodsID == 101) {
			goodsShareIcon = R.drawable.demo_dsxg02;
			tvGoodsDetail.setText(R.string.goods_sug02_name);
			tvGoodsPrice.setText(R.string.goods_sug02_price);
			ivGoodsIcon.setImageResource(R.drawable.demo_dsxg02_big);
			goodsSug01.setVisibility(View.VISIBLE);
			goodsSug02.setVisibility(View.GONE);
			goodsSug03.setVisibility(View.VISIBLE);
		} else if (goodsID == 102) {
			goodsShareIcon = R.drawable.demo_dsxg03;
			tvGoodsDetail.setText(R.string.goods_sug03_name);
			tvGoodsPrice.setText(R.string.goods_sug03_price);
			ivGoodsIcon.setImageResource(R.drawable.demo_dsxg03_big);
			goodsSug01.setVisibility(View.VISIBLE);
			goodsSug02.setVisibility(View.VISIBLE);
			goodsSug03.setVisibility(View.GONE);
		}
	}

	private void getMobIDToShare() {
		if (mobIdCache.containsKey(goodsID)) {
			mobID = String.valueOf(mobIdCache.get(goodsID));
			if (!TextUtils.isEmpty(mobID)) {
				share();
				return;
			}
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("goodsID", goodsID);
		MobLink.getMobID(params, CommonUtils.SHOPPING_PATH, CommonUtils.SHOPPING_SOURCE, new ActionListener() {
			public void onResult(HashMap<String, Object> params) {
				if (params != null && params.containsKey("mobID")) {
					mobID = String.valueOf(params.get("mobID"));
					mobIdCache.put(goodsID, mobID);
					Log.i("Get mobID success ==>> ", mobID);
				} else {
					Toast.makeText(ShoppingDetailActivity.this, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
				}
				share();
			}

			public void onError(Throwable t) {
				if (t != null) {
					Toast.makeText(ShoppingDetailActivity.this, "error = " + t.getMessage(), Toast.LENGTH_SHORT).show();
				}
				share();
			}
		});
	}

	private void share() {
		String shareUrl = CommonUtils.SHARE_URL + CommonUtils.SHOPPING_PATH + "/" + goodsID;
		if (!TextUtils.isEmpty(mobID)) {
			shareUrl += "/?mobid=" + mobID;
		}
		String title = tvGoodsDetail.getText().toString();
		String text = tvGoodsDetail.getText().toString();
		String imgPath = CommonUtils.copyImgToSD(this, goodsShareIcon, "goods_" + goodsID);
		CommonUtils.showShare(this, title, text, shareUrl, imgPath);
	}
}
