package com.mob.moblink.demo.restore.model;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.Scene;

import java.util.HashMap;

public class RestoreModel {

	public void getMobId(String path, HashMap<String, Object> params, final OnGetMobIdListener listener) {
		Scene s = new Scene();
		s.setPath(path);
		s.setParams(params);
		MobLink.getMobID(s, new ActionListener<String>() {
			@Override
			public void onResult(String mobID) {
				if (listener != null) {
					listener.onResult(mobID);
				}
			}

			@Override
			public void onError(Throwable t) {
				if (listener != null) {
					listener.onError(t);
				}
			}
		});
	}

	public interface OnGetMobIdListener {
		void onResult(String mobId);
		void onError(Throwable t);
	}
}
