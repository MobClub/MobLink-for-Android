package com.mob.moblink.demo.util;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mob.moblink.demo.model.AnalyzeRecord;
import com.mob.moblink.demo.model.BaseData;
import com.mob.moblink.demo.model.DemoUser;
import com.mob.moblink.demo.model.FriendList;
import com.mob.moblink.demo.model.JoinRoom;
import com.mob.moblink.demo.model.PushInfo;
import com.mob.moblink.demo.net.HttpCallBack;
import com.mob.moblink.demo.net.NetworkHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

public class DemoAsyncProtocol {

	private static final String QUERY_FRIEND = "/friend/query";
	private static final String LOGIN = "/user/login";
	private static final String USER_INFO = "/user/info";
	private static final String QUERY_PUSH = "/push/query";
	private static final String JOIN_ROOM = "/game/joinRoom";
	private static final String EXIT_ROOM = "/game/exitRoom";
	private static final String HEART_ROOM = "/game/read";
	private static final String RECORD = "/record";
	private static final String SCENE_LOG = "/scene/log";
	private static final String ADD_FRIEND = "/friend/add";
	private static final String DEL_FRIEND = "/friend/del";
	private static final String ADD_MONEY_PUSH = "/push/addMoneyPush";

	private static NetworkHelper networkHelper;
	private static Gson gson;
	private static String userId;

	private static void ensureInit() {
		if (null == networkHelper) {
			networkHelper = new NetworkHelper();
		}
		if (null == gson) {
			gson = new Gson();
		}
	}

	private static String getServerUrl(String path) {
		return CommonUtils.getDemoUrl() + path;
	}

	private static HashMap<String, Object> getParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (null == userId){
			userId = SPHelper.getDemoUserId();
		}
		params.put("id", userId);
		return params;
	}

	/**
	 * 查看用户信息
	 */
	public static void getUserInfo(final DemoAsyncListener<DemoUser> listener) {
		ensureInit();
		String userid = SPHelper.getDemoUserId();
		if(TextUtils.isEmpty(userid)) {
			login(listener);
			return;
		} else {
			userId = userid;
		}
		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(USER_INFO)), getParams(), new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, DemoUser.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	private static void login(final DemoAsyncListener<DemoUser> listener) {
		ensureInit();
		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(LOGIN)), new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						DemoUser user = gson.fromJson(s, DemoUser.class);
						userId = user.getRes().id;
						SPHelper.setDemoUserId(userId);
						listener.onSuccess(user);
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	/**
	 * 好友列表
	 */
	public static void friendList(final DemoAsyncListener<FriendList> listener) {
		ensureInit();
		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(QUERY_FRIEND)), getParams(), new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, FriendList.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}
			@Override
			public void onCancel() {

			}
		});

	}

	public static void addFriend(String addId, Integer channel, final DemoAsyncListener<DemoUser> listener) {
		ensureInit();
		HashMap<String, Object> params = getParams();
		params.put("addId", addId);
		if (null != channel) {
			params.put("channel", channel);
		}

		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(ADD_FRIEND)), params, new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, DemoUser.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	public static void delFriend(String addId, final DemoAsyncListener<DemoUser> listener) {
		ensureInit();
		HashMap<String, Object> params = getParams();
		params.put("addId", addId);
		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(DEL_FRIEND)), params, new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, DemoUser.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	/**
	 * 加入房间/创建房间
	 * @param roomId 为null时创建新房间
	 */
	public static void joinRoom(Integer roomId, final DemoAsyncListener<JoinRoom> listener) {
		ensureInit();
		HashMap<String, Object> params = getParams();
		if (null != roomId) {
			params.put("roomId", roomId);
		}

		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(JOIN_ROOM)), params, new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, JoinRoom.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	public static void exitRoom(final DemoAsyncListener<Integer> listener) {
		ensureInit();
		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(EXIT_ROOM)), getParams(), new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						Hashon hashon = new Hashon();
						HashMap<String, Object> map = hashon.fromJson(s);
						listener.onSuccess(ResHelper.forceCast(map.get("success"), Integer.valueOf(0)));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	/**
	 * 房间心跳
	 */
	public static void readRoom(int roomId, final DemoAsyncListener<JoinRoom> listener) {
		ensureInit();
		HashMap<String, Object> params = getParams();
		params.put("roomId", roomId);
		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(HEART_ROOM)), params, new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, JoinRoom.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	/**
	 *查询自己推了多少用户
	 */
	public static void queryPushInfo(final DemoAsyncListener<PushInfo> listener) {
		ensureInit();
		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(QUERY_PUSH)), getParams(), new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, PushInfo.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	/**
	 * 推广记录
	 */
	public static void record(int type, final DemoAsyncListener<AnalyzeRecord> listener) {
		ensureInit();
		HashMap<String, Object> params = getParams();
		params.put("type", type);
		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(RECORD)), params, new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, AnalyzeRecord.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	/**
	 * 场景还原记录提交
	 */
	public static void sceneLog(String sourceId, int scene, final DemoAsyncListener<BaseData> listener) {
		ensureInit();
		HashMap<String, Object> params = getParams();
		params.put("sourceId", sourceId);
		params.put("scene", scene);
		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(SCENE_LOG)), params, new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, BaseData.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}

	/**
	 *  地推/社交分享
	 * @param sourceId
	 * @param listener
	 */
	public static void addMoneyPush(String sourceId, String channel, Integer type, final DemoAsyncListener<BaseData> listener) {
		ensureInit();
		HashMap<String, Object> params = getParams();
		params.put("sourceId", sourceId);
		if (null != channel) {
			params.put("channel", channel);
		}
		if (null != type) {
			params.put("type", type);
		}

		networkHelper.asyncConnect(CommonUtils.checkHttpRequestUrl(getServerUrl(ADD_MONEY_PUSH)), params, new HttpCallBack<String>() {
			@Override
			public void onStart(String url) {

			}

			@Override
			public void onLoading(long progress, long count) {

			}

			@Override
			public void onSuccess(final String s) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onSuccess(gson.fromJson(s, BaseData.class));
						return false;
					}
				});
			}

			@Override
			public void onFailure(final int responseCode, final Throwable e) {
				UIHandler.sendEmptyMessage(0, new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						listener.onFailure(responseCode, e);
						return false;
					}
				});
			}

			@Override
			public void onCancel() {

			}
		});
	}
}
