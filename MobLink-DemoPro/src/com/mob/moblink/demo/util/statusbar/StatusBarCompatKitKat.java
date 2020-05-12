package com.mob.moblink.demo.util.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * After kitkat add fake status bar
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class StatusBarCompatKitKat {

	private static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";
	private static final String TAG_MARGIN_ADDED = "marginAdded";

	/**
	 * return statusBar's Height in pixels
	 */
	private static int getStatusBarHeight(Context context) {
		int result = 0;
		int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resId > 0) {
			result = context.getResources().getDimensionPixelOffset(resId);
		}
		return result;
	}

	/**
	 * 1. Add fake statusBarView.
	 * 2. set tag to statusBarView.
	 */
	private static View addFakeStatusBarView(Activity activity, int statusBarColor, int statusBarHeight) {
		Window window = activity.getWindow();
		ViewGroup mDecorView = (ViewGroup) window.getDecorView();

		View mStatusBarView = new View(activity);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
		layoutParams.gravity = Gravity.TOP;
		mStatusBarView.setLayoutParams(layoutParams);
		mStatusBarView.setBackgroundColor(statusBarColor);
		mStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);

		mDecorView.addView(mStatusBarView);
		return mStatusBarView;
	}

	/**
	 * use reserved order to remove is more quickly.
	 */
	private static void removeFakeStatusBarViewIfExist(Activity activity) {
		Window window = activity.getWindow();
		ViewGroup mDecorView = (ViewGroup) window.getDecorView();

		View fakeView = mDecorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
		if (fakeView != null) {
			mDecorView.removeView(fakeView);
		}
	}

	/**
	 * add marginTop to simulate set FitsSystemWindow true
	 */
	private static void addMarginTopToContentChild(View mContentChild, int statusBarHeight) {
		if (mContentChild == null) {
			return;
		}
		if (!TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
			lp.topMargin += statusBarHeight;
			mContentChild.setLayoutParams(lp);
			mContentChild.setTag(TAG_MARGIN_ADDED);
		}
	}

	/**
	 * remove marginTop to simulate set FitsSystemWindow false
	 */
	private static void removeMarginTopOfContentChild(View mContentChild, int statusBarHeight) {
		if (mContentChild == null) {
			return;
		}
		if (TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
			lp.topMargin -= statusBarHeight;
			mContentChild.setLayoutParams(lp);
			mContentChild.setTag(null);
		}
	}

	/**
	 * set StatusBarColor
	 * <p>
	 * 1. set Window Flag : WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
	 * 2. removeFakeStatusBarViewIfExist
	 * 3. addFakeStatusBarView
	 * 4. addMarginTopToContentChild
	 * 5. cancel ContentChild's fitsSystemWindow
	 */
	static void setStatusBarColor(Activity activity, int statusColor) {
		Window window = activity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
		View mContentChild = mContentView.getChildAt(0);
		int statusBarHeight = getStatusBarHeight(activity);

		removeFakeStatusBarViewIfExist(activity);
		addFakeStatusBarView(activity, statusColor, statusBarHeight);
		addMarginTopToContentChild(mContentChild, statusBarHeight);

		if (mContentChild != null) {
			mContentChild.setFitsSystemWindows(false);
		}
	}

	/**
	 * translucentStatusBar
	 * <p>
	 * 1. set Window Flag : WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
	 * 2. removeFakeStatusBarViewIfExist
	 * 3. removeMarginTopOfContentChild
	 * 4. cancel ContentChild's fitsSystemWindow
	 */
	static void translucentStatusBar(Activity activity) {
		Window window = activity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
		View mContentChild = mContentView.getChildAt(0);

		removeFakeStatusBarViewIfExist(activity);
		removeMarginTopOfContentChild(mContentChild, getStatusBarHeight(activity));
		if (mContentChild != null) {
			mContentChild.setFitsSystemWindows(false);
		}
	}

}
