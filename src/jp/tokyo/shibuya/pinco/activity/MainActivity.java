package jp.tokyo.shibuya.pinco.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.tokyo.shibuya.pinco.R;
import jp.tokyo.shibuya.pinco.abs.AbsListActivity;
import jp.tokyo.shibuya.pinco.adapter.FeedAdapter;
import jp.tokyo.shibuya.pinco.dto.FeedDto;
import jp.tokyo.shibuya.pinco.entity.FeedEntity;
import jp.tokyo.shibuya.pinco.jsonutil.api.Feed;
import jp.tokyo.shibuya.pinco.util.PreferenceUtil;
import jp.tokyo.shibuya.pinco.util.Util;

import org.json.JSONException;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AbsListActivity implements OnScrollListener {
	
	GetFeedTask feedTask;
	private Map<String, Boolean> map;
	private String FOOTER = "gFooter";
	private ArrayAdapter<FeedEntity> adapter;
	private ArrayList<FeedEntity> dataList;
	private String nextPageId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed);

		mListView = getListView();
		mListView.setScrollingCacheEnabled(false);
		mListView.setVerticalFadingEdgeEnabled(false);
		
		gFooter.setVisibility(View.GONE);
		mListView.addFooterView(gFooter);
		map = new HashMap<String, Boolean>();
		map.put(FOOTER, true);
		
		if (Util.isEmpty(PreferenceUtil.getAccessToken(this))) {
			Intent intent = new Intent(this, AuthenticationActivity.class);
			startActivity(intent);
		}
		
		mListView.setOnScrollListener(this);
		
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (feedTask != null && feedTask.getStatus() == AsyncTask.Status.RUNNING) return;
		if (totalItemCount == firstVisibleItem + visibleItemCount) {
			
			if (totalItemCount >= 6) {
				gFooter.setVisibility(View.VISIBLE);
				additionalReading();
			} else {
				gFooter.setVisibility(View.GONE);
			}
			
		}
	}
	
	private void additionalReading() {

		int dataListCount = 0;
		if (dataList != null) {
			dataListCount = dataList.size();
		}
		if (dataListCount < 6 || dataList.size() == 0) {
			gFooter.setVisibility(View.GONE);
			// gFooter2.setVisibility(View.GONE);
			return;
		} else {
			feedTask = new GetFeedTask(this, mListView, gFooter, 1);
			feedTask.execute();
			return;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		if (!Util.isEmpty(PreferenceUtil.getAccessToken(this))) {
//			TextView tView = (TextView) findViewById(R.id.token);
//			tView.setText(PreferenceUtil.getAccessToken(this).toString());
//			Log.i("Instagram", "token : " + PreferenceUtil.getAccessToken(this));
//		}

		// ネットワークに接続れているかをチェックする
		try {
			// ネットワーク接続チェック
			networkConnectionCheck();
			// データ取得タスクの実行
			feedTask = new GetFeedTask(this, mListView, gFooter, 0);
			feedTask.execute();
		} catch (NetworkErrorException e) {
			Toast.makeText(this, R.string.msg_nw_disconnected, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		Log.d("onPrepareOptionsMenu", "雨後イターーーー");
		return super.onPrepareOptionsMenu(menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			return rootView;
		}
	}

	class GetFeedTask extends AsyncTask<Void, FeedDto, FeedDto> {
		
		private ListView mListView;
		private View mFooter;
		private int patern;
		private Context context;
		private int count = 0;
		
		public GetFeedTask(Context context, ListView mListView, View footer, int patern) {
			this.context = context;
			this.mListView = mListView;
			this.mFooter = footer;
			this.patern = patern;
		}
		
		@Override
		protected void onPreExecute() {
//			if (patern == 0) {
//				dialog = onCreateDialog(Const.DIALOG_GET_DATA_LOADING);
//				dialog.show();
//			}
		}

		@Override
		protected FeedDto doInBackground(Void... params) {
			FeedDto dto = null;
			if (patern == 0) {
				try {
					dto = Feed.get(MainActivity.this);
				} catch (UnsupportedEncodingException | JSONException e) {
					e.printStackTrace();
				}
			} else if (patern == 1 && !Util.isEmpty(nextPageId)) {
				try {
					dto = Feed.get(MainActivity.this, nextPageId);
				} catch (UnsupportedEncodingException | JSONException e) {
					e.printStackTrace();
				}
			}
			
			if (dto != null) {
				dataList = dto.dataList;
				nextPageId = dto.nextMaxId;
			} else {
				dataList = null;
				nextPageId = null;
			}

			return dto;
		}
		
		@Override
		protected void onPostExecute(FeedDto dto) {
			if (dto == null || dto.dataList.size() == 0) return;
			count = dto.dataList.size();
			
			if (patern == 0) {
				adapter = new FeedAdapter(context, dto.dataList);
				try {
					setListAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (count == 6) {
					gFooter.setVisibility(View.GONE);
				} else {
					mListView.removeFooterView(gFooter);
					map.put(FOOTER, false);
				}
				
			} else if (patern == 1) {
				for (int i = 0; i < count; i++) {
					(adapter).insert(dto.dataList.get(i), adapter.getCount());
				}
				mListView.invalidateViews();
				
				if (count == 6) {
					gFooter.setVisibility(View.GONE);
				} else {
					mListView.removeFooterView(gFooter);
					map.put(FOOTER, false);
				}
			}
		}

	}

}
