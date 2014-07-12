package jp.tokyo.shibuya.pinco.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
	
	public static void saveAccessToken(Context context, String code) {
		SharedPreferences pref = context.getSharedPreferences("AUTHORIZATION", Context.MODE_PRIVATE);
		pref.edit().putString("access_token", code).commit();
	}
	
	public static String getAccessToken(Context context) {
		SharedPreferences pref = context.getSharedPreferences("AUTHORIZATION", Context.MODE_PRIVATE);
		return pref.getString("access_token", "");
	}
	
	public static void saveAuthCode(Context context, String code) {
		SharedPreferences pref = context.getSharedPreferences("AUTHORIZATION", Context.MODE_PRIVATE);
		pref.edit().putString("code", code).commit();
	}
	
	public static String getAuthCode(Context context) {
		SharedPreferences pref = context.getSharedPreferences("AUTHORIZATION", Context.MODE_PRIVATE);
		return pref.getString("code", "");
	}
	
	public static void saveUserAgent(Context context, String ua) {
		SharedPreferences pref = context.getSharedPreferences("USER_AGENT", Context.MODE_PRIVATE);
		pref.edit().putString("ua", ua).commit();
	}
	
	public static String getUserAgent(Context context) {
		SharedPreferences pref = context.getSharedPreferences("USER_AGENT", Context.MODE_PRIVATE);
		return pref.getString("ua", "");
	}

}
