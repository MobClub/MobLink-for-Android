package com.mob.moblink.demo.app;

import android.content.Intent;

import com.mob.MobApplication;
import com.mob.moblink.demo.service.MobLinkService;


public class DemoApplication extends MobApplication {
	private static final String TAG = "DemoApplication";

	@Override
	public void onCreate() {
		super.onCreate();

		// 开启service(模拟包含service的app)
		Intent intent = new Intent(this, MobLinkService.class);
		startService(intent);
	}
}
