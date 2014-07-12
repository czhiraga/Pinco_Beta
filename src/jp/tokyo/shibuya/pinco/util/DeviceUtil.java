package jp.tokyo.shibuya.pinco.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DeviceUtil {

	private static String ipShape = "^(\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[01]?\\d\\d|2[0-4]\\d|25[0-5])$";

	/**
	 * AndroidId(端末識別ID)を取得する
	 * 
	 * @param activity
	 * @return androidId
	 */
	public static String getAndroidId(Context context) {

		String androidId = Settings.Secure.getString(
				context.getContentResolver(), Settings.System.ANDROID_ID);
		if (androidId == null || "".equals(androidId)) {
			return "";
		}
		return androidId;
	}

	/**
	 * IMEI(端末識別ID)を取得する
	 * 
	 * @param activity
	 * @return IMEI
	 */
	public static String getIMEI(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context
				.getSystemService("phone");
		String deviceId = mTelephonyMgr.getDeviceId();
		if (deviceId == null || "".equals(deviceId)) {
			deviceId = "";
		}
		return deviceId;
	}

	/**
	 * WifiのMacアドレス取得
	 * 
	 * @param activity
	 * @return
	 */
	public static String getWifiMacAddress(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = manager.getConnectionInfo();
		String macAddress = wifiInfo.getMacAddress();
		if (macAddress == null || "".equals(macAddress)) {
			macAddress = "";
		}
		return macAddress;
	}

	/**
	 * 強制的にWifiMacAddressを取得する（WifiがOffの場合、強制的に一瞬だけONにする）
	 * 
	 * @param context
	 * @return
	 */
	public static String getMacAdress(final Context context) {
		String macAddress = null;
		boolean wifiOnFlg = false;
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (macAddress == null) {
			macAddress = wifiInfo.getMacAddress();
		}

		if (macAddress == null || "".equals(macAddress)) {
			wifiManager.setWifiEnabled(true); // wifi ON
			wifiOnFlg = true;
		}

		while (true) {
			wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			// MacAdress取得
			macAddress = wifiManager.getConnectionInfo().getMacAddress();
			
			if (macAddress != null && !"".equals(macAddress)) {
				if (wifiOnFlg) {
					wifiManager.setWifiEnabled(false); // wifi off
				}
				break;
			} else {
				try {
					// Logger.d(TAG, "sleep 1 second getMacAdressNoThread....");
					Thread.sleep(2000); // 取得出来ない場合、2秒後に再度スレッド起動
				} catch (Exception e) {
					// Logger.d(TAG, e.getMessage(), e);
				}
			}
		}
		// 最初から小文字に統一しておく
		// (OSがアップデートしたら大文字小文字が変動する可能性がある)
		macAddress = macAddress.toLowerCase();
		
		return macAddress;
	}

	/**
	 * 3GとWifiのうちアクティブなIPアドレスを取得する
	 * 
	 * @param context
	 * @return String IPAddress
	 */
	public static String getActiveIpAddress(Context context) {
		// 接続方法取得
		String status = getActiveNetwork(context);
		if ("3G".equals(status)) {
			return getIPAddress();

		} else if ("WIFI".equals(status)) {
			return getWifiIPAddress(context);
		}

		return "127.0.0.1";
	}
	
	/**
	 * その瞬間アクティブなネットワークを取得する
	 * @param context
	 * @return 3G / WIFI
	 */
	public static String getActiveNetwork(Context context) {

		// 3Gの状態を取得する
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ネットワーク情報を全て取得
		NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
		for (NetworkInfo ni : networkInfos) {
			if (ni.getType() == ConnectivityManager.TYPE_MOBILE
					&& ni.isConnected()) {

				return "3G";
			} else if (ni.getType() == ConnectivityManager.TYPE_WIFI
					&& ni.isConnected()) {

				return "WIFI";
			}
		}
		return "";
	}

	/**
	 * 3G回線のIPアドレスを取得する
	 * 
	 * @return
	 */
	private static String getIPAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();

			while (interfaces.hasMoreElements()) {
				NetworkInterface network = interfaces.nextElement();
				Enumeration<InetAddress> addresses = network.getInetAddresses();

				while (addresses.hasMoreElements()) {
					String address = addresses.nextElement().getHostAddress();
					if (address.matches(ipShape)) {
						return address;
					}
					// 127.0.0.1と0.0.0.0以外のアドレスが見つかったらそれを返す
					// if (!"127.0.0.1".equals(address)
					// && !"0.0.0.0".equals(address)) {
					// return address;
					// }
				}
			}
		} catch (IOException e) {
			return "127.0.0.1";
		} catch (Exception e) {
			return "127.0.0.1";
		}

		return "127.0.0.1";
	}

	/**
	 * WifiのIPアドレスを取得する
	 * 
	 * @param context
	 * @return
	 */
	private static String getWifiIPAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String strIPAddess = ((ipAddress >> 0) & 0xFF) + "."
				+ ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF)
				+ "." + ((ipAddress >> 24) & 0xFF);
		if (!"".equals(strIPAddess)) {
			return strIPAddess;
		}
		return "127.0.0.1";
	}

	/**
	 * ユーザーエージェントの取得
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserAgent(Context context) {
		String userAgent = PreferenceUtil.getUserAgent(context);
		// String userAgent = "";
		if ("".equals(userAgent)) {
			WebView webView = new WebView(context);
			WebSettings webSettings = webView.getSettings();
			userAgent = webSettings.getUserAgentString();
			PreferenceUtil.saveUserAgent(context, userAgent);
		}
		return userAgent;
	}

}
