package jp.tokyo.shibuya.pinco.activity;

import java.io.UnsupportedEncodingException;

import jp.tokyo.shibuya.pinco.R;
import jp.tokyo.shibuya.pinco.entity.AuthenticationEntity;
import jp.tokyo.shibuya.pinco.jsonutil.api.Authenticate;
import jp.tokyo.shibuya.pinco.util.Settings;
import jp.tokyo.shibuya.pinco.util.PreferenceUtil;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthenticationActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authenticate_activity);
		
		WebView webView = (WebView) findViewById(R.id.auth_webview);
		setWebView(webView);
	}
	
	private void setWebView(WebView webView) {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setAppCacheEnabled(true);
		
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				
			}
			
			
		});
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				if (url != null) {
					if (checkUrl(url)) finish();
				}
				return false;
			}
		});
		
		webView.loadUrl(Settings.INSTA_OAUTH_URL);
	}
	
	/**
	 * URLのチェック<br>
	 * アクセストークンの取得
	 * @param url String
	 * @return
	 */
	private boolean checkUrl(String url) {
		if (url == null || url.indexOf("https://instagram.com/oauth/authorize") != -1 || url.indexOf(Settings.REDIRECT_URI) == -1) return false;
		
		String paramStr = url.replace(Settings.REDIRECT_URI, "");
		if (paramStr.indexOf("?") != -1) paramStr = paramStr.replace("?", "");
		if (paramStr.indexOf("#") != -1) paramStr = paramStr.replace("#", "");
		
		String[] params = paramStr.split("&");
		for(String param : params) {
			String[] keyValue = param.split("=");
			if ("code".equals(keyValue[0])) PreferenceUtil.saveAuthCode(AuthenticationActivity.this, keyValue[1]);
			if ("access_token".equals(keyValue[0])) PreferenceUtil.saveAccessToken(AuthenticationActivity.this, keyValue[1]);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
