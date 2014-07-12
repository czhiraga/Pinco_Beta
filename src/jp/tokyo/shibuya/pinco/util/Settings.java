package jp.tokyo.shibuya.pinco.util;

public class Settings {
	
	public static final String CLIENT_ID     = "01db1665e2a44619b21c0c48e3a8d5fa";
	public static final String CLIENT_SECRET = "b2e8729722ed4de6af02e116fa365511";
	public static final String REDIRECT_URI  = "pinco://authentication";
	
	public static final String HTTP  = "http";
	public static final String HTTPS = "https";
	/** Instagram API Domain */
	public static final String INSTA_API_DOMAIN = "api.instagram.com";
	/** Instagram API Version */
	public static final String INSTA_API_VERSION = "v1";
	
	/** Instagram OAuth認証用URL */
//	public static final String OAUTH_URL     = "https://api.instagram.com/oauth/authorize/?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=code&scope=basic+relationships+comments+likes";
	public static final String INSTA_OAUTH_URL = "https://" + INSTA_API_DOMAIN + "/oauth/authorize/?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=token&scope=basic+relationships+comments+likes";
	
	/* API Method **********************************/
	
	/** 指定したユーザーの情報 */
	public static final String API_USER_INFO = "/users/";
	/** 自分のFeed */
	public static final String API_SELF_FEED = "/users/self/feed";  
	
	
	
	
	
}
