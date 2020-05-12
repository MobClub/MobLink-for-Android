package com.mob.moblink.demo.util;

import com.mob.MobSDK;
import com.mob.moblink.MobLink;
import com.mob.tools.proguard.ClassKeeper;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.SharePrefrenceHelper;

public class SPHelper implements ClassKeeper, PublicMemberKeeper {
	private static final int DB_VERSION = 1;
	private static final String KEY_USER_ID = "demo_user_id";
	private static final String KEY_DEMO_FIRST = "demo_first";
	private static final String KEY_DEMO_MONEY_PUSH = "first_add_money_push";

	private static SharePrefrenceHelper sp;

	private static void ensureInit() {
		if (sp == null) {
			sp = new SharePrefrenceHelper(MobSDK.getContext());
			sp.open(MobLink.getSdkTag(), DB_VERSION);
		}
	}

	public static synchronized void setDemoFirst(boolean hasFirst) {
		ensureInit();
		sp.putBoolean(KEY_DEMO_FIRST, hasFirst);
	}

	public static synchronized boolean demoHasFirst() {
		ensureInit();
		return sp.getBoolean(KEY_DEMO_FIRST);
	}

	public static synchronized void setDemoAddMoneyPush(boolean hasPush) {
		ensureInit();
		sp.putBoolean(KEY_DEMO_MONEY_PUSH, hasPush);
	}

	public static synchronized boolean demoHasFirstAddMoneyPush() {
		ensureInit();
		return sp.getBoolean(KEY_DEMO_MONEY_PUSH);
	}

	/**
	 * demo用户id
	 */
	public static synchronized void setDemoUserId(String data) {
		ensureInit();
		sp.putString(KEY_USER_ID, data);
	}

	public static synchronized String getDemoUserId() {
		ensureInit();
		return sp.getString(KEY_USER_ID);
	}
}
