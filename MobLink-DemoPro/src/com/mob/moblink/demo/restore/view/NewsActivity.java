package com.mob.moblink.demo.restore.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.moblink.demo.BaseActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsActivity extends BaseActivity {

	private ListView lvNewsList;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_news;
	}

	@Override
	protected int getTitleId() {
		return R.string.today_news;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		rightIv.setVisibility(View.INVISIBLE);
		lvNewsList = (ListView) findViewById(R.id.lv_news);

		MyAdapter adapter = new MyAdapter(this);
		adapter.setData(CommonUtils.getNewsData(this));
		lvNewsList.setAdapter(adapter);
		lvNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Toast.makeText(NewsActivity.this, "click position == " + position, Toast.LENGTH_SHORT).show();
				Intent i = new Intent(NewsActivity.this, NewsDetailActivity.class);
				i.putExtra("position", position);
				startActivity(i);
			}
		});
		
		
	}

	private class MyAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private ArrayList<HashMap<String, Object>> data;
		public MyAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}
		
		public void setData(ArrayList<HashMap<String, Object>> data) {
			this.data = data;
		}
		
		public int getCount() {
			if (data != null) {
				return 3;
			}
			return 0;
		}

		public Object getItem(int position) {
			if (data != null) {
				return  data.get(position);
			}
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.layout_news_item, null);
				holder.ivNewsIcon = (ImageView) convertView.findViewById(R.id.iv_news_icon);
				holder.tvNewsTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
				holder.tvNewsTop = (TextView) convertView.findViewById(R.id.tv_set_news_top);
				holder.tvNewsOpic = (TextView) convertView.findViewById(R.id.tv_news_opic);
				holder.tvNewsComms = (TextView) convertView.findViewById(R.id.tv_news_comments);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.ivNewsIcon.setImageResource((Integer) data.get(position).get("img"));
			holder.tvNewsTitle.setText((String) data.get(position).get("title"));
			holder.tvNewsComms.setText((String) data.get(position).get("comm"));
			boolean top = (Boolean) data.get(position).get("top");
			boolean opic = (Boolean) data.get(position).get("opic");
			if (top) {
				holder.tvNewsTop.setVisibility(View.VISIBLE);
			} else {
				holder.tvNewsTop.setVisibility(View.GONE);
			}
			
			if (opic) {
				holder.tvNewsOpic.setVisibility(View.VISIBLE);
			} else {
				holder.tvNewsOpic.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		
		class ViewHolder {
			public ImageView ivNewsIcon;
			public TextView tvNewsTitle;
			public TextView tvNewsTop;
			public TextView tvNewsOpic;
			public TextView tvNewsComms;
		}
	}
	
}
