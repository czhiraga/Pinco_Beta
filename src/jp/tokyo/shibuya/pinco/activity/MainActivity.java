package jp.tokyo.shibuya.pinco.activity;

import jp.tokyo.shibuya.pinco.R;
import jp.tokyo.shibuya.pinco.util.PreferenceUtil;
import jp.tokyo.shibuya.pinco.util.Util;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
		if (!Util.isEmpty(PreferenceUtil.getAccessToken(this))) {
			TextView tView = (TextView) findViewById(R.id.token);
			tView.setText(PreferenceUtil.getAccessToken(this).toString());
			Log.i("Instagram","token : " + PreferenceUtil.getAccessToken(this));
		}
		
		if (Util.isEmpty(PreferenceUtil.getAccessToken(this))) {
			Intent intent = new Intent(this, AuthenticationActivity.class);
			startActivity(intent);
		}
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			
			return rootView;
		}
	}
	
//	class AuthenticateTask extends AsyncTask<Void, Void, Void> {
//		
//		
//		@Override
//		protected Void doInBackground(Void... params) {
//			
//			try {
//				AuthenticationEntity entity = Authenticate.getAccessToken(MainActivity.this, PreferenceUtil.getAuthCode(MainActivity.this));
//			} catch (UnsupportedEncodingException | JSONException e) {
//				e.printStackTrace();
//			}
//			
//			return null;
//		}
//		
//	}
}
