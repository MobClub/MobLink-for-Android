package com.mob.moblink.demo.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by wang_hh on 2019/5/7.
 */

public class PermissionHelper {

	public static boolean hasPermissionInManifest(Context context, String permission){
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_PERMISSIONS);
			String[] requestedPermissions = packageInfo.requestedPermissions;
			for(String permissionStr:requestedPermissions){
				if(TextUtils.equals(permission,permissionStr)){
					return true;
				}
			}

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
