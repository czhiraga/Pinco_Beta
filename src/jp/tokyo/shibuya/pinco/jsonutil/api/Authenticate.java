package jp.tokyo.shibuya.pinco.jsonutil.api;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import jp.tokyo.shibuya.pinco.entity.AuthenticationEntity;
import jp.tokyo.shibuya.pinco.util.Constants;
import jp.tokyo.shibuya.pinco.util.GetJson;
import jp.tokyo.shibuya.pinco.util.Settings;
import jp.tokyo.shibuya.pinco.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri.Builder;

public class Authenticate {
	
	public static AuthenticationEntity getAccessToken(Context context, String code) throws UnsupportedEncodingException,
	   JSONException {
		if (Util.isEmpty(code)) return null;
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("client_id", Constants.CLIENT_ID);
		map.put("client_secret", Constants.CLIENT_SECRET);
		map.put("grant_type", "authorization_code");
		map.put("redirect_uri", Constants.REDIRECT_URI);
		map.put("code", code);
		
		return getDataAndParse(context, map);
	}
	
	private static AuthenticationEntity getDataAndParse(Context context, Map<String,String> map)
										throws JSONException,
											   UnsupportedEncodingException {
		
		//接続先情報
		Builder builder = new Builder();
		builder.scheme(Settings.HTTPS);
		builder.encodedAuthority(Settings.INSTA_API_DOMAIN);
		builder.path("oauth/access_token");
		
		//サーバからJsonファイルを取得
		JSONObject jObject = GetJson.getJson(builder, map, 2, context);
		
		// エラーチェック
//		ApiUtility.checkApiStatus(jObject);
		
//		JSONObject resultObject = jObject.getJSONObject(Const.J_RESULT);
//		String statusCode = String.valueOf(jObject.getInt(Const.J_STATUS));
		
		return null;
	}
	
}


