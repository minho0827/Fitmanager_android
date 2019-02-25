package com.fitmanager.app.util;

/**
 * Return에 대한 Static Int형식의 RequestCodeUtil
 * @author 유민호
 * @since 2018.06.18
 */
public class RequestCodeUtil {
	
	public static class ActivityReqCode {
		public static final int REQ_PAYMENT_RETURN_FRAGMENT = 1;
		public static final int REQ_SETTING_RETURN_FRAGMENT = 3;
	}
	
	public static class PermissionReqCode {
		public static final int REQ_PERMISSION_ALL = 9999;
	}

	public static class WIFIReqCode {
		public static final int REQ_WIFI_CHECK = 11;
	}

	public static class GooglePlayServiceCheckReqCode {
		public static final int REQ_GOOGLE_CHECK = 5384;
	}

	public static class FCMReqCode {
		public static final int REQ_SEND_MESSAGE = 111;
	}
}
