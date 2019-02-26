package com.mob.moblink.demo.restore.view;

public interface IRestoreView {
	void onMobIdGot(String mobId);
	void onMobIdError(Throwable t);
}
