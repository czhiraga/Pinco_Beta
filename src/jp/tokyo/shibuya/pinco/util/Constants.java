package jp.tokyo.shibuya.pinco.util;

public class Constants {

	public static final String CLIENT_ID     = "01db1665e2a44619b21c0c48e3a8d5fa";
	public static final String CLIENT_SECRET = "b2e8729722ed4de6af02e116fa365511";
	public static final String REDIRECT_URI  = "pinco://authentication";

	/** OAuth認証用URL */
//	public static final String OAUTH_URL     = "https://api.instagram.com/oauth/authorize/?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=code&scope=basic+relationships+comments+likes";
	public static final String OAUTH_URL     = "https://api.instagram.com/oauth/authorize/?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=token&scope=basic+relationships+comments+likes";

	/** pixiv API */
	public static final String PIXIV_SEARCH_API = "http://spapi.pixiv.net/iphone/search.php?";
	public static final String PIXIV_RANKING_API = "http://spapi.pixiv.net/iphone/ranking.php?";
}
