package com.mob.moblink.demo.util;

import android.util.Log;

public class MLog {
	private static final String MARK = "[MOBLINK]";

	public static int d(String tag, String msg) {
		return Log.d(tag, MARK + msg);
	}

	public static int d(String tag, String msg, Throwable t) {
		return Log.d(tag, MARK + msg, t);
	}

	public static int i(String tag, String msg) {
		return Log.i(tag, MARK + msg);
	}

	public static int i(String tag, String msg, Throwable t) {
		return Log.i(tag, MARK + msg, t);
	}

	public static int w(String tag, String msg) {
		return Log.w(tag, MARK + msg);
	}

	public static int w(String tag, String msg, Throwable t) {
		return Log.w(tag, MARK + msg, t);
	}

	public static int e(String tag, String msg) {
		return Log.e(tag, MARK + msg);
	}

	public static int e(String tag, String msg, Throwable t) {
		return Log.e(tag, MARK + msg, t);
	}
}
