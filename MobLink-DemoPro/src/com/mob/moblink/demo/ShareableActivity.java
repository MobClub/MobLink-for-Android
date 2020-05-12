package com.mob.moblink.demo;

import android.app.Dialog;
import android.content.Intent;

import com.mob.moblink.MobLink;
import com.mob.moblink.Scene;
import com.mob.moblink.SceneRestorable;
import com.mob.moblink.demo.model.BaseData;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.DemoAsyncListener;
import com.mob.moblink.demo.util.DemoAsyncProtocol;
import com.mob.moblink.demo.util.SPHelper;

import java.util.Map;

public abstract class ShareableActivity extends BaseActivity implements SceneRestorable {
	private Dialog dialog;
	private String path;
	private String paramStr = "";

	@Override
	public void onReturnSceneData(Scene scene) {
		if (scene != null) {
			path = scene.getPath();
			paramStr = "";
			if (scene.getParams() != null) {
				String sourceId = null;
				if (scene.getParams().containsKey("sourceId")) {
					sourceId = String.valueOf(scene.getParams().get("sourceId"));
				} else if (scene.getParams().containsKey("id")) {
					sourceId = String.valueOf(scene.getParams().get("id"));
				}

				//添加收益用户-首次启动获取到地堆/社会分享场景
				if (!SPHelper.demoHasFirstAddMoneyPush()) {
					if (CommonUtils.LOCAL_INVITE_PATH.equals(path) || CommonUtils.SHARE_INVITE_PATH.equals(path)) {
						String channel = null;
						if (scene.getParams().containsKey("channel")) {
							channel = String.valueOf(scene.getParams().get("channel"));
						}
						Integer type = 1;
						if (CommonUtils.SHARE_INVITE_PATH.equals(path)) {
							type = 2;
						}
						DemoAsyncProtocol.addMoneyPush(sourceId, channel, type, new DemoAsyncListener<BaseData>() {

							@Override
							public void onSuccess(BaseData baseData) {
								if (null != baseData && baseData.success == 1) {
									SPHelper.setDemoAddMoneyPush(true);
								}
							}

							@Override
							public void onFailure(int responseCode, Throwable e) {

							}
						});
					}
				}
				//场景还原记录提交
				Integer sceneId = null;
				if (scene.getParams().containsKey("scene")) {
					sceneId = Integer.valueOf(String.valueOf(scene.getParams().get("scene")));
				}
				if (null == sceneId) {
					sceneId = getSceneId(path);
				}
				if (null != sceneId) {
					DemoAsyncProtocol.sceneLog(sourceId, sceneId, new DemoAsyncListener<BaseData>() {
						@Override
						public void onSuccess(BaseData baseData) {

						}

						@Override
						public void onFailure(int responseCode, Throwable e) {

						}
					});
				}

				for (Map.Entry<String, Object> entry : scene.getParams().entrySet()) {
					paramStr += (entry.getKey() + " : " + entry.getValue() + "\r\n");
				}
			}

			// dialog不能复用, 防止参数更换, 不能及时更新
			if (null != dialog && dialog.isShowing()) {
				dialog.dismiss();
			}
			dialog = CommonUtils.getDialog(this, scene.getPath(), paramStr);
			if (!dialog.isShowing()) {
//				dialog.show();
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		MobLink.updateNewIntent(getIntent(), this);
	}

	private Integer getSceneId(String path) {
		Integer scene = null;
		if (CommonUtils.NEWS_PATH.equals(path)) {
			scene = CommonUtils.SCENE_NEWS;
		} else if (CommonUtils.NOVEL_PATH.equals(path)) {
			scene = CommonUtils.SCENE_NOVEL;
		} else if (CommonUtils.GAME_PATH.equals(path)) {
			scene = CommonUtils.SCENE_GAME;
		} else if (CommonUtils.LOCAL_INVITE_PATH.equals(path)) {
			scene = CommonUtils.SCENE_LOCAL_INVITE;
		} else if (CommonUtils.SHARE_INVITE_PATH.equals(path)) {
			scene = CommonUtils.SCENE_SHARE_INVITE;
		} else if (CommonUtils.MATCH_PATH.equals(path)) {
			scene = CommonUtils.SCENE_MATCH;
		}
		return scene;
	}

	/**
	 * 显示参数
	 */
	protected void showParamDialog() {
		if (dialog == null) {
			dialog = CommonUtils.getDialog(this, path, paramStr);
		}
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}
}
