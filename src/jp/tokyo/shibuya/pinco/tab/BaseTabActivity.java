package jp.tokyo.shibuya.pinco.tab;

import jp.tokyo.shibuya.pinco.R;
import jp.tokyo.shibuya.pinco.activity.MainActivity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class BaseTabActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base_tab_activity);

		// tabhost
		LocalActivityManager mlam = new LocalActivityManager(this, false);
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup(mlam);
		
		TabSpec tab1 = tabHost.newTabSpec("feed");
		tab1.setContent(new Intent().setClass(this, MainActivity.class));
		tab1.setIndicator("Feed");
		tabHost.addTab(tab1);
		
		TabSpec tab2 = tabHost.newTabSpec("search");
		tab2.setIndicator("Search");
		tab2.setContent(new Intent().setClass(this, MainActivity.class));
		tabHost.addTab(tab2);
		
		TabSpec tab3 = tabHost.newTabSpec("watch");
		tab3.setIndicator("Watch");
		tab3.setContent(new Intent().setClass(this, MainActivity.class));
		tabHost.addTab(tab3);
		
		TabSpec tab4 = tabHost.newTabSpec("setting");
		tab4.setIndicator("Setting");
		tab4.setContent(new Intent().setClass(this, MainActivity.class));
		tabHost.addTab(tab4);
		
		tabHost.setCurrentTab(0);
	}
	
	

}
