package jp.tokyo.shibuya.pinco.jsonutil.api;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.tokyo.shibuya.pinco.dto.FeedDto;
import jp.tokyo.shibuya.pinco.entity.Caption;
import jp.tokyo.shibuya.pinco.entity.FeedData;
import jp.tokyo.shibuya.pinco.entity.FeedEntity;
import jp.tokyo.shibuya.pinco.entity.Images;
import jp.tokyo.shibuya.pinco.util.GetJson;
import jp.tokyo.shibuya.pinco.util.PreferenceUtil;
import jp.tokyo.shibuya.pinco.util.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri.Builder;
import android.util.Log;

public class Feed {
	
	public static FeedDto get(Context context) throws UnsupportedEncodingException,
	   JSONException {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("access_token", PreferenceUtil.getAccessToken(context));
		return getDataAndParse(context, map);
	}
	
	/**
	 * 次ページデータ取得
	 * @param context
	 * @param nextMaxId
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JSONException
	 */
	public static FeedDto get(Context context, String nextMaxId) throws UnsupportedEncodingException,
	   JSONException {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("access_token", PreferenceUtil.getAccessToken(context));
		map.put("max_id", nextMaxId);
		return getDataAndParse(context, map);
	}
	
	private static FeedDto getDataAndParse(Context context, Map<String,String> map)
										throws JSONException,
											   UnsupportedEncodingException {
		
		//接続先情報
		Builder builder = new Builder();
		builder.scheme(Settings.HTTPS);
		builder.encodedAuthority(Settings.INSTA_API_DOMAIN);
		builder.path(Settings.INSTA_API_VERSION + Settings.API_SELF_FEED);
		
		//サーバからJsonファイルを取得
		JSONObject resultObject = GetJson.getJson(builder, map, 1, context);
		
		// ステータスコードチェック
		int statusCode = resultObject.getJSONObject("meta").getInt("code");
		if (statusCode != HttpURLConnection.HTTP_OK) return null;
		
		/* pagination*****/
		FeedDto dto = new FeedDto();
		JSONObject pagination = resultObject.getJSONObject("pagination");
		dto.nextUrl = pagination.getString("next_url");
		dto.nextMaxId = pagination.getString("next_max_id");
		dto.dataList = new ArrayList<FeedEntity>();
		
		JSONArray dataArray = resultObject.getJSONArray("data");
		
		int j = 0;
		FeedEntity entity = new FeedEntity();
		for (int i = 0; i < dataArray.length(); i++) {
			FeedData feedData = new FeedData();
			
			/* data *****/
			JSONObject data = dataArray.getJSONObject(i);
			feedData.feedId = data.getString("id");
			feedData.tag = data.getString("tags");
			feedData.type = data.getString("type");
			feedData.filter = data.getString("filter");
			feedData.createdTime = data.getString("created_time");
			feedData.link = data.getString("link");
			
			/* images *****/
			Images image = new Images();
			JSONObject images = data.getJSONObject("images");
			// low_resolution
			JSONObject lowResolution = images.getJSONObject("low_resolution");
			String lowResolutionUrl = lowResolution.getString("url");
			String lowResolutionWidth = lowResolution.getString("width");
			String lowResolutionHeight = lowResolution.getString("height");
			image.setLowResolution(lowResolutionUrl, lowResolutionWidth, lowResolutionHeight);
			// thumbnail
			JSONObject thumbnail = images.getJSONObject("thumbnail");
			String thumbnailUrl = thumbnail.getString("url");
			String thumbnailWidth = thumbnail.getString("width");
			String thumbnailHeight = thumbnail.getString("height");
			image.setThumbnail(thumbnailUrl, thumbnailWidth, thumbnailHeight);
			// standard_resolution
			JSONObject standardResolution = images.getJSONObject("standard_resolution");
			String standardResolutionUrl = standardResolution.getString("url");
			String standardResolutionWidth = standardResolution.getString("width");
			String standardResolutionHeight = standardResolution.getString("height");
			image.setStandardResolution(standardResolutionUrl, standardResolutionWidth, standardResolutionHeight);
			feedData.images = image;
			
			/* caption *****/
			Caption caption = new Caption();
			JSONObject captions = data.getJSONObject("caption");
			String captionId = captions.getString("id");
			String captionCreatedTime = captions.getString("created_time");
			String captionText = captions.getString("text");
			// from
			JSONObject from = captions.getJSONObject("from");
			String captionUserName = from.getString("username");
			String captionUserId = from.getString("id");
			String profilePicture = from.getString("profile_picture");
			String fullName = from.getString("full_name");
			caption.setCaption(captionId, captionCreatedTime, captionText, captionUserName, captionUserId, profilePicture, fullName);
			feedData.caption = caption;
			Log.d("カウント","i : " + i);
			
			if (j == 0) entity.feedData1 = feedData;
			if (j == 1) entity.feedData2 = feedData;
			if (j == 2) {
				entity.feedData3 = feedData;
				// リスト追加
				dto.dataList.add(entity);
				entity = new FeedEntity();
				j = 0;
			} else {
				j++;
			}
		}
		
		return dto;
	}
	
}


