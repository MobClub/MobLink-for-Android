package com.mob.moblink.demo.splash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.moblink.demo.R;
import com.mob.tools.gui.ViewPagerAdapter;

public class SplashViewPagerAdapter extends ViewPagerAdapter {

	private int[] titleIds = new int[]{R.string.moblink_demo_splash_title1, R.string.moblink_demo_splash_title2, R.string.moblink_demo_splash_title3};
	private int[] descIds = new int[]{R.string.moblink_demo_splash_desc1, R.string.moblink_demo_splash_desc2, R.string.moblink_demo_splash_desc3};
	private int[] picIds = new int[]{R.drawable.moblink_demo_splash_1, R.drawable.moblink_demo_splash_2, R.drawable.moblink_demo_splash_3};

	private Context context;
	private ISplashView iSplashView;

	public SplashViewPagerAdapter(Context context, ISplashView iSplashView) {
		this.context = context;
		this.iSplashView = iSplashView;
	}

	@Override
	public int getCount() {
		return titleIds.length;
	}

	@Override
	public void onScreenChange(int currentScreen, int lastScreen) {
		super.onScreenChange(currentScreen, lastScreen);
		iSplashView.onScreenChange(currentScreen, lastScreen);
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if (null == view) {
			view = LayoutInflater.from(context).inflate(R.layout.moblink_demo_splash_view_item, viewGroup, false);
		}
		ImageView picIv = (ImageView) view.findViewById(R.id.moblink_demo_splash_picIv);
		TextView titleTv = (TextView) view.findViewById(R.id.moblink_demo_splash_titleTv);
		TextView descTv = (TextView) view.findViewById(R.id.moblink_demo_splash_descTv);
		Button gotoBtn = (Button) view.findViewById(R.id.moblink_demo_splash_goto);

		picIv.setImageResource(picIds[i]);
		titleTv.setText(titleIds[i]);
		descTv.setText(descIds[i]);

		if (i == titleIds.length - 1) {
			gotoBtn.setVisibility(View.VISIBLE);
		} else {
			gotoBtn.setVisibility(View.INVISIBLE);
		}

		gotoBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iSplashView.gotoMainActivity();
			}
		});

		return view;
	}
}
