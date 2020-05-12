package com.mob.moblink.demo.app;

import android.app.Activity;
import android.content.Intent;

import com.mob.MobApplication;
import com.mob.moblink.MobLink;
import com.mob.moblink.RestoreSceneListener;
import com.mob.moblink.Scene;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.Map;


public class DemoApplication extends MobApplication {
	private static final String TAG = "DemoApplication";

	@Override
	public void onCreate() {
		super.onCreate();
		MobLink.setRestoreSceneListener(new SceneListener());
	}

	class SceneListener extends Object implements RestoreSceneListener {

		@Override
		public Class<? extends Activity> willRestoreScene(Scene scene) {
			String path = scene.getPath();
			Map<String, Class<? extends Activity>> pathMap = CommonUtils.PATH_MAP_LOCAL;
			if (pathMap.keySet().contains(path)) {
				return pathMap.get(path);
			}

			// 后台配置, 让moblik sdk去处理
			pathMap = CommonUtils.PATH_SERVER_MAP;
			if (pathMap.keySet().contains(path)) {
				return null;
			}

			return null;
		}

		@Override
		public void notFoundScene(Scene scene) {
		}

		@Override
		public void completeRestore(Scene scene) {

		}
	}
}
