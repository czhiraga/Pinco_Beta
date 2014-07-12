package jp.tokyo.shibuya.pinco.jsonutil.api;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import jp.tokyo.shibuya.pinco.entity.AuthenticationEntity;
import jp.tokyo.shibuya.pinco.util.GetJson;
import jp.tokyo.shibuya.pinco.util.PreferenceUtil;
import jp.tokyo.shibuya.pinco.util.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri.Builder;

public class Feed {
	
	public static AuthenticationEntity get(Context context) throws UnsupportedEncodingException,
	   JSONException {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("access_token", PreferenceUtil.getAccessToken(context));
		return getDataAndParse(context, map);
	}
	
	private static AuthenticationEntity getDataAndParse(Context context, Map<String,String> map)
										throws JSONException,
											   UnsupportedEncodingException {
		
		//接続先情報
		Builder builder = new Builder();
		builder.scheme(Settings.HTTPS);
		builder.encodedAuthority(Settings.INSTA_API_DOMAIN);
		builder.path(Settings.INSTA_API_VERSION + Settings.API_SELF_FEED);
		
		//サーバからJsonファイルを取得
		JSONObject jObject = GetJson.getJson(builder, map, 1, context);
		
		// エラーチェック
//		ApiUtility.checkApiStatus(jObject);
		
//		JSONObject resultObject = jObject.getJSONObject(Const.J_RESULT);
//		String statusCode = String.valueOf(jObject.getInt(Const.J_STATUS));
		
		return null;
	}
	
}


