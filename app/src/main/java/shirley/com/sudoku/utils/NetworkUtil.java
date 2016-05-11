package shirley.com.sudoku.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * 网络状态判断工具类
 * 
 * @author chenxl
 */
public class NetworkUtil {

	public static final String CTWAP = "ctwap";
	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";
	public static String WAP_IP;
	
	public static enum NET_TYPE{
		/** 网络不可用 */
		TYPE_NET_WORK_DISABLED,
		/** 移动联通wap10.0.0.172 */
		TYPE_CM_CU_WAP,
		/** 电信wap 10.0.0.200 */
		TYPE_CT_WAP,
		/** wifi网络 */
		TYPE_WIFI,
		/** 电信,移动,联通 等net网络 */
		TYPE_MOBILE
	}
	
	private static final String LOG_TAG = NetworkUtil.class.getName();

	public static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");
	public static int previousType = 0;

	/**
	 * 判断网络类型
	 * @param context
	 * @return
	 */
	public static NET_TYPE getNetType(Context mContext) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				return NET_TYPE.TYPE_NET_WORK_DISABLED;
			} else {
				int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					return NET_TYPE.TYPE_WIFI;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
//					final Cursor c = mContext.getContentResolver().query(
//							PREFERRED_APN_URI, null, null, null, null);
//					if (c != null) {
//						c.moveToFirst();
//						final String user = c.getString(c
//								.getColumnIndex("user"));
//						if (!TextUtils.isEmpty(user)) {
//							if (user.startsWith(CTWAP)) {
//								WAP_IP = "10.0.0.200";
//								return NET_TYPE.TYPE_CT_WAP;
//							}
//						}
//					}
//					c.close();
//					String netMode = mobNetInfoActivity.getExtraInfo();
//
//					if (netMode != null) {
//						netMode = netMode.toLowerCase();
//						if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
//								|| netMode.equals(UNIWAP)) {
//							WAP_IP = "10.0.0.172";
//							return NET_TYPE.TYPE_CM_CU_WAP;
//						} else {
//						}
//					}
							return NET_TYPE.TYPE_MOBILE;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return NET_TYPE.TYPE_NET_WORK_DISABLED;
		}
		return NET_TYPE.TYPE_WIFI;
	}
}