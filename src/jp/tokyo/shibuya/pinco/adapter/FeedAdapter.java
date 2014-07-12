package jp.tokyo.shibuya.pinco.adapter;

import java.util.ArrayList;

import jp.tokyo.shibuya.pinco.R;
import jp.tokyo.shibuya.pinco.entity.FeedEntity;
import jp.tokyo.shibuya.pinco.holder.FeedHolder;
import jp.tokyo.shibuya.pinco.util.ImageGetTask;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FeedAdapter extends ArrayAdapter<FeedEntity> {
	
	private Context context;
	private ArrayList<FeedEntity> items;
	private FeedEntity item;
	private FeedHolder holder;
	

	public FeedAdapter(Context context, ArrayList<FeedEntity> list) {
		super(context, R.layout.list_row_feed, R.id.dummy_text, list);
		this.context = context;
		this.items = list;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		item = items.get(position);
		View view = super.getView(position, convertView, parent);
		
		holder = (FeedHolder) view.getTag();
		
		ListView list = null;
		
		try {
			list = (ListView) parent;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if (holder == null) {
			holder = new FeedHolder(view);
			view.setTag(holder);
		} else {
			holder = (FeedHolder) view.getTag();
		}
		
		holder.pBar1.setVisibility(View.VISIBLE);
		holder.imageView1.setVisibility(View.GONE);
		holder.pBar2.setVisibility(View.VISIBLE);
		holder.imageView2.setVisibility(View.GONE);
		holder.pBar3.setVisibility(View.VISIBLE);
		holder.imageView3.setVisibility(View.GONE);
		
		try {
			// Image1
			holder.imageView1.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nophoto));
			holder.imageView1.setTag(item.feedData1.images.thumbnailUrl);
			ImageGetTask task1 = new ImageGetTask(holder.imageView1, holder.pBar1, false);
			task1.execute(item.feedData1.images.thumbnailUrl);
			
			// Image2
			holder.imageView2.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nophoto));
			holder.imageView2.setTag(item.feedData2.images.thumbnailUrl);
			ImageGetTask task2 = new ImageGetTask(holder.imageView2, holder.pBar2, false);
			task2.execute(item.feedData2.images.thumbnailUrl);

			// Image3
			holder.imageView3.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_nophoto));
			holder.imageView3.setTag(item.feedData3.images.thumbnailUrl);
			ImageGetTask task3 = new ImageGetTask(holder.imageView3, holder.pBar3, false);
			task3.execute(item.feedData3.images.thumbnailUrl);
			
		} catch(Exception e) {
			e.printStackTrace();
			holder.pBar1.setVisibility(View.GONE);
			holder.imageView1.setVisibility(View.VISIBLE);
			holder.pBar2.setVisibility(View.GONE);
			holder.imageView2.setVisibility(View.VISIBLE);
			holder.pBar3.setVisibility(View.GONE);
			holder.imageView3.setVisibility(View.VISIBLE);
		}
		
		
		return view;
	}

}
