package jp.tokyo.shibuya.pinco.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri.Builder;
import android.util.Log;

public class GetJson {
	
	/**
	 * URLからJsonObjectを取得する
	 * @param url
	 * @return
	 * @throws JSONException
	 */
//	public static JSONObject getJson (String url) throws JSONException {
//		return new JSONObject(HttpUtil.getJson(url));
//	}
	
	/**
	 * 
	 * @param builder 宛先URL情報
	 * @param getData 送信データ
	 * @param connectType 1:GET 2:POST 3:MULTI PART
	 * @param con コンテキスト
	 * @return JSONオブジェクト
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject getJson(Builder builder, Map<String, String> getData, int connectType, Context con)
			throws JSONException, UnsupportedEncodingException {
			String json = "";
			if (connectType == 1) json = getJson(builder, getData, null, connectType, con);
			if (connectType == 2 || connectType == 4) json = getJson(builder, null, getData, connectType, con);
			if (json == null) {
				return null;
			}
		return new JSONObject(json);
	}
	
	/**
	 * 
	 * @param builder
	 * @param getData
	 * @param connectType
	 * @param con
	 * @return JSONArray
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public static JSONArray getJsonArray(Builder builder, Map<String, String> getData, int connectType, Context con) throws JSONException, UnsupportedEncodingException {
		String json = "";
		if (connectType == 1) json = getJson(builder, getData, null, connectType, con);
		if (connectType == 2 || connectType == 4) json = getJson(builder, null, getData, connectType, con);
		if (json == null) {
			return null;
		}
		return new JSONArray(json);
	}
	
	/**
	 * 指定した接続先からJSONを受け取る (JSONを取得する際の共通メソッド)
	 * @param builder URL
	 * @param getData GETパラメータ
	 * @param postData　POSTパラメータ
	 * @param connectType 1〜4 1:GET 2:POST 3:MultiPart 4:GET&POST(Hybrid)
	 * @param con コンちゃん
	 * @return
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public static String getJson(Builder builder, Map<String, String> getData, Map<String, String> postData, int connectType, Context con)
					throws JSONException, UnsupportedEncodingException {
		
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedReader reader = null;
		
		try {
			conn = getConnection(builder, getData, postData, connectType, con);
			int statusCode = conn.getResponseCode();
			Log.d("取得したJSON", "Response：" + statusCode);
			// Log.i(LOG_TAG, "statusCode:" + statusCode);
			switch (statusCode) {
			// 200
			case HttpURLConnection.HTTP_OK:
				break;
			// 404
			case HttpURLConnection.HTTP_NOT_FOUND:
				
				break;
			default:
				
				break;
			}
			
			boolean isGzip = isGzip(conn);
			if (isGzip == true) {
				reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream()), "UTF-8"));
			} else {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			}
			
			// レスポンスを取得
			StringBuilder responseBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line);
			}
			String result = responseBuilder.toString();
			Log.d("取得したJSON", "JSON：" + result);
			
			return result;
			
		} catch (SocketTimeoutException e) {
			Log.e("GetJson / timeout error", "通信がタイムアウト", e);
		} catch (Throwable t) {
			t.getStackTrace();
			t.getMessage();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// Log.w(LOG_TAG, "failed to close reader.", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.w("GetJson / failed to close connection.", e.getMessage());
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}
	
	public static HttpURLConnection getConnection(Builder builder, Map<String, String> getData, Map<String, String> postData, int connectType, Context con) {
		String urlString = "";
		/** HttpGetでJSONを取得 ------------ */
		if ((connectType == 1 || connectType == 4) && getData != null) {

			for (Entry<String, String> entry : getData.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				builder.appendQueryParameter(key, value);
			}
		}
		urlString = builder.build().toString();

//		} else if (2 <= connectType) {

//			urlString = builder.build().toString();
//		}
		Log.d("指定したURL", urlString);
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedReader reader = null;
		
		try {
			
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestProperty("Accept",
//					"application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
//			conn.setRequestProperty("Accept-Charset", "UTF-8,*;q=0.5");
//			conn.setRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");
//			// Gzip対応 本物っぽいUserAgentを指定しないとなぜかgzipが効かない
//			conn.setRequestProperty("User-Agent", DeviceUtil.getUserAgent(con));
//			conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			
			if (connectType == 1) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				OutputStream os = null;
				try {
					os = conn.getOutputStream();
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));

					if (connectType == 2 || connectType == 4) {
//						writeRequestBody(os, postData);
						writeRequestBody(writer, postData);
					}
					os.flush();
				} catch (Exception e) {
					e.getStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							// Log.w(LOG_TAG, "failed to close output stream.");
						}
					}
				}
			}
			
			// 接続する
			conn.connect();
		} catch (SocketTimeoutException e) {
			Log.e("GetJson / timeout error", "通信がタイムアウト", e);
		} catch (Throwable t) {
			t.getStackTrace();
			t.getMessage();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// Log.w(LOG_TAG, "failed to close reader.", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.w("GetJson / failed to close connection.", e.getMessage());
				}
			}
//			if (conn != null) {
//				conn.disconnect();
//			}
		}
		return conn;
	}
	
	public static JSONObject getJson(Builder builder, Map<String, String> getData, Map<String, String> postData, int connectType, Context con, String userAgent)
			throws JSONException, UnsupportedEncodingException {

		String urlString = "";
		/** HttpGetでJSONを取得 ------------ */
		if ((connectType == 1 || connectType == 4) && getData != null) {
		
			for (Entry<String, String> entry : getData.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				builder.appendQueryParameter(key, value);
			}
		}
		urlString = builder.build().toString();
		
		//} else if (2 <= connectType) {
		
		//	urlString = builder.build().toString();
		//}
		Log.d("指定したURL", urlString);
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedReader reader = null;
		
		try {
			
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Accept",
					"application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
			conn.setRequestProperty("Accept-Charset", "UTF-8,*;q=0.5");
			conn.setRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");
			// Gzip対応 本物っぽいUserAgentを指定しないとなぜかgzipが効かない
			conn.setRequestProperty("User-Agent", userAgent);
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			
			if (connectType == 1) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				OutputStream os = null;
				try {
					os = conn.getOutputStream();
		
					if (connectType == 2 || connectType == 4) {
						writeRequestBody(os, postData);
					}
					os.flush();
				} catch (Exception e) {
					e.getStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							// Log.w(LOG_TAG, "failed to close output stream.");
						}
					}
				}
			}
			
			// 接続する
			conn.connect();
			int statusCode = conn.getResponseCode();
			Log.d("取得したJSON", "Response：" + statusCode);
			// Log.i(LOG_TAG, "statusCode:" + statusCode);
			switch (statusCode) {
			// 200
			case HttpURLConnection.HTTP_OK:
				break;
			// 404
			case HttpURLConnection.HTTP_NOT_FOUND:
				
				break;
			default:
				
				break;
			}
			
			boolean isGzip = isGzip(conn);
			if (isGzip == true) {
				reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream()), "UTF-8"));
			} else {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			}
			
			// レスポンスを取得
			StringBuilder responseBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line);
			}
			String result = responseBuilder.toString();
			Log.d("取得したJSON", "JSON：" + result);
			
			return new JSONObject(result);
			
		} catch (SocketTimeoutException e) {
			Log.e("GetJson / timeout error", "通信がタイムアウト", e);
		} catch (Throwable t) {
			t.getStackTrace();
			t.getMessage();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// Log.w(LOG_TAG, "failed to close reader.", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.w("GetJson / failed to close connection.", e.getMessage());
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
		}
	
	/**
	 * 通常のPostリクエストのRequestBodyを書き込む
	 * 
	 * @param os
	 * @param postData
	 * @throws IOException
	 */
	private static void writeRequestBody(OutputStream os, Map<String, String> postData) throws IOException {
		// Mapの中身をbuilderに入れる
		StringBuilder builder = new StringBuilder();
		int cnt = 0;
		for (Entry<String, String> entry : postData.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			if (cnt != 0) {
				builder.append("&");
			}
			builder.append(name + "=" + value);
			cnt++;
		}
		os.write(builder.toString().getBytes());
	}
	
	private static void writeRequestBody(BufferedWriter writer, Map<String, String> postData) throws IOException {
		// Mapの中身をbuilderに入れる
		StringBuilder builder = new StringBuilder();
		int cnt = 0;
		for (Entry<String, String> entry : postData.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();
			if (cnt != 0) {
				builder.append("&");
			}
			builder.append(name + "=" + value);
			cnt++;
		}
		writer.write(builder.toString());
	}
	
	/**
	 * GZIPを使っているかチェックする
	 * 
	 * @param conn
	 * @return
	 */
	public static boolean isGzip(HttpURLConnection conn) {
		boolean usingGZIP = false;
		List<String> contentEncodings = getHeaderValues(conn, "content-encoding");
		if (contentEncodings != null) {
			for (int i = 0; i < contentEncodings.size(); ++i) {
				String contentEncoding = contentEncodings.get(i);
				if (contentEncoding.equalsIgnoreCase("gzip")) {
					usingGZIP = true;
					break;
				}
			}
		}

		return usingGZIP;
	}
	
	/**
	 * ヘッダーのValue一覧を取得する
	 * 
	 * @param conn
	 * @param name
	 * @return
	 */
	public static List<String> getHeaderValues(HttpURLConnection conn, String name) {

		// レスポンスヘッダーのKeyを全てループする(大文字小文字の区別が怪しいため)
		Map<String, List<String>> headerFields = conn.getHeaderFields();
		for (Entry<String, List<String>> entry : headerFields.entrySet()) {
			String headerKey = entry.getKey();
			if (name.equalsIgnoreCase(headerKey)) {
				List<String> values = headerFields.get(headerKey);
				return values;
			}
		}
		return null;
	}
}
