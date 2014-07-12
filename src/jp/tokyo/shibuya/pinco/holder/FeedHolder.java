package jp.tokyo.shibuya.pinco.holder;

import jp.tokyo.shibuya.pinco.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class FeedHolder {
	
	public LinearLayout ll;
	public ImageView imageView1;
	public ImageView imageView2;
	public ImageView imageView3;
	
	public ProgressBar pBar1;
	public ProgressBar pBar2;
	public ProgressBar pBar3;
	
	public FeedHolder(View view) {
		ll = (LinearLayout) view.findViewById(R.id.feed_row);
		imageView1 = (ImageView) view.findViewById(R.id.img1);
		imageView2 = (ImageView) view.findViewById(R.id.img2);
		imageView3 = (ImageView) view.findViewById(R.id.img3);
		
		pBar1 = (ProgressBar) view.findViewById(R.id.pbar1);
		pBar2 = (ProgressBar) view.findViewById(R.id.pbar2);
		pBar3 = (ProgressBar) view.findViewById(R.id.pbar3);
	}

}
