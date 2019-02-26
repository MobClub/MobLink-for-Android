package com.mob.moblink.demo.restore.presenter;

import android.app.Activity;

import com.mob.moblink.demo.restore.model.RestoreModel;
import com.mob.moblink.demo.restore.view.IRestoreView;
import com.mob.moblink.demo.common.ShareHelper;

import java.util.HashMap;

public class RestorePresenter {
	private IRestoreView iRestoreView;
	private RestoreModel restoreModel;

	public RestorePresenter(IRestoreView iRestoreView) {
		this.iRestoreView = iRestoreView;
		this.restoreModel = new RestoreModel();
	}

	public void getMobId(String path, HashMap<String, Object> params) {
		restoreModel.getMobId(path, params, new RestoreModel.OnGetMobIdListener() {
			@Override
			public void onResult(String mobId) {
				if (iRestoreView != null) {
					iRestoreView.onMobIdGot(mobId);
				}
			}

			@Override
			public void onError(Throwable t) {
				if (iRestoreView != null) {
					iRestoreView.onMobIdError(t);
				}
			}
		});
	}

	public void share(Activity activity, String title, String text, String shareUrl, String imgPath) {
		ShareHelper.showShareReal(activity, title, text, shareUrl, imgPath);
	}
}
