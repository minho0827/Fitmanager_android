package com.fitmanager.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 권한 목록을 가져오고 수행하는 Class
 * @author 유민호
 * @since 2018.03.30
 */
public class PermissionUtil {
	
	/**
	 * 요청이 필요한 권한 목록을 getRequiredPermissions 메서드를 통해 구한 후 사용자에게 실제 권한 요청을 수행한다. 
	 ************************************************************************************************************************************************/
	public static boolean checkAndRequestPermission(Activity activity, int permissionCode, String... permissions) {
	    String[] requiredPermissions = getRequiredPermissions(activity, permissions);

	    if (requiredPermissions.length > 0) {
	    	// 6.0 이하 버전에서는 이 결과가 무조건 '허용'으로 처리되기 때문에 
	    	// Activity의 requestPermissions에서 무조건 '허용' 처리된다.
	        ActivityCompat.requestPermissions(activity, requiredPermissions, permissionCode);
	        return false;
	    } else {
	        return true;
	    }
	}
	
	/**
	 * 요청이 필요한 권한 목록 가져오기
	 ************************************************************************************************************************************************/
	private static String[] getRequiredPermissions(Context context, String ... permissions) {
		List<String> requirePermissionList = new ArrayList<>();
		
		// Context가 null이면 무조건 권한을 요청하도록 requiredPermissions가 존재한다고 reutrn 한다
	    if (context == null) 
	    	return requirePermissionList.toArray(new String[1]);

	    for (String permission : permissions) {
	        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
	        	requirePermissionList.add(permission);
	    }

	    return requirePermissionList.toArray(new String[requirePermissionList.size()]);
	}
	
	/**
	 * 사용자가 권한 허용/거부 했는지 체크하는 메서드
	 ************************************************************************************************************************************************/
	public static boolean verifyPermissions(int[] grantResults) {
		// grantResults가 존재하지 않으면 '거부' 처리된 것으로 함.
	    if (grantResults.length < 1)
	    	return false;

	    for (int result : grantResults) {
	    	// 요청 권한 중에 하나라도 '거부'했다면 전체를 '거부' 처리한 것으로 함.
	        if (result != PackageManager.PERMISSION_GRANTED)
	        	return false;
	    }
	    return true;
	}
}
