package com.mob.moblink.demo.util;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.moblink.demo.R;
import com.mob.tools.utils.ResHelper;

public class ShareOrVCodeDialog extends Dialog {
	private TextView btnShare;
	private TextView btnGetVCode;
	private TextView btnCancel;

	public ShareOrVCodeDialog(Context context) {
		super(context, R.style.DialogBottomToTop);
		init(context);
	}

	private void init(Context context) {
		setCanceledOnTouchOutside(true);//触摸屏幕取消窗体
		setCancelable(true);//按返回键取消窗体

		//setup view
		LinearLayout containLayout = new LinearLayout(context);
		containLayout.setOrientation(LinearLayout.VERTICAL);
		int padding = ResHelper.dipToPx(context, 10);
		containLayout.setPadding(padding, 0, padding, padding);
		setContentView(containLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		LinearLayout itemContainer = new LinearLayout(context);
		itemContainer.setOrientation(LinearLayout.VERTICAL);
		itemContainer.setBackgroundColor(0x00FFFFFF);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		containLayout.addView(itemContainer, llp);

		//添加item
		int dividerHeight = ResHelper.dipToPx(context, 1);
		int itemHeight = ResHelper.dipToPx(context, 45);
		int txtSize = ResHelper.dipToPx(context, 15);
		llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
		btnShare = createItemView(context, 1, txtSize);
		btnShare.setBackgroundResource(R.drawable.demo_btn_corner_white_up);
		btnShare.setText(R.string.share_to_third_platform);
		itemContainer.addView(btnShare, llp);

		View vLine1 = new View(context);
		vLine1.setBackgroundColor(0xFFEEEEEE);
		llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight);
		itemContainer.addView(vLine1, llp);

		btnGetVCode = createItemView(context, 2, txtSize);
		btnGetVCode.setBackgroundResource(R.drawable.demo_btn_corner_white_down);
		btnGetVCode.setText(R.string.get_qr_code);
		llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
		itemContainer.addView(btnGetVCode, llp);

		btnCancel = createItemView(context, 3, txtSize);
		btnCancel.setBackgroundResource(R.drawable.demo_btn_corner_white);
		btnCancel.setText(R.string.btn_cancel);
		llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
		llp.topMargin = padding;
		itemContainer.addView(btnCancel, llp);
	}

	private TextView createItemView(Context context, int tag, int txtSize) {
		TextView tvItem = new TextView(context);
		tvItem.setTag(tag);
		tvItem.setGravity(Gravity.CENTER);
		tvItem.setTextColor(0xFF1A1A1A);
		tvItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtSize);
		return tvItem;
	}

	public void show() {
		super.show();
		//设置宽度全屏，需在show之后
		WindowManager.LayoutParams wlp = getWindow().getAttributes();
		wlp.gravity = Gravity.BOTTOM;
		wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
		wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(wlp);
	}

	public ShareOrVCodeDialog setOnItemClickListener(final OnItemClickListener itemClickListener) {
		View.OnClickListener ocl = new View.OnClickListener() {
			public void onClick(View v) {
				if (itemClickListener != null) {
					int position = ResHelper.forceCast(v.getTag());
					if (position < 3) {
						itemClickListener.onItemClick(position);
					}
				}
				dismiss();
			}
		};
		btnShare.setOnClickListener(ocl);
		btnGetVCode.setOnClickListener(ocl);
		btnCancel.setOnClickListener(ocl);
		return this;
	}

	public static abstract class OnItemClickListener {
		/**
		 * dialog 的item click回调方法
		 *
		 * @param position 由上而下从1开始，1分享，2二维码
		 */
		public abstract void onItemClick(int position);
	}

}
