package jp.tokyo.shibuya.pinco.abs;

import jp.tokyo.shibuya.pinco.R;
import android.accounts.NetworkErrorException;
import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class AbsListActivity extends ListActivity {
	
	protected ListView mListView;
	/** 追加読み込み用のフッタ */
	protected View gFooter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** 追加読み込み用フッタ */
		gFooter = getLayoutInflater().inflate(R.layout.list_footer, null);
	}
	
	
	
	/**
	 * ネットワークの接続チェック
	 * 
	 * @return 接続結果を返す <br>
	 *         true 接続されている<br>
	 *         false　未接続
	 * 
	 */
	public void networkConnectionCheck() throws NetworkErrorException {
		// システムから接続情報をとってくる
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// モバイル回線（３G）の接続状態を取得
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		// wifiの接続状態を取得
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

		// 3Gデータ通信／wifi共に接続状態じゃない場合
		if ((mobile != State.CONNECTED) && (wifi != State.CONNECTED)) {
			// ネットワーク未接続
			Toast.makeText(this, R.string.msg_nw_disconnected, Toast.LENGTH_SHORT).show();
			// ネットワーク接続エラースロー
			throw new NetworkErrorException();
		}
	}
}
