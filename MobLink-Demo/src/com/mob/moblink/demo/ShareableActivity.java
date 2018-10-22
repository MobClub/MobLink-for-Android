package com.mob.moblink.demo;

import android.app.Dialog;

import com.mob.moblink.Scene;
import com.mob.moblink.SceneRestorable;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.Map;

public class ShareableActivity extends BaseActivity implements SceneRestorable {
	private Dialog dialog;
	private String path;
	private String paramStr = "";

	@Override
	public void onReturnSceneData(Scene scene) {
		if (scene != null) {
			path = scene.getPath();
			paramStr = "";
			if (scene.getParams() != null) {
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
				dialog.show();
			}
		}
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
