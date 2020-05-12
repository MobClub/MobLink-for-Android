package com.mob.moblink.demo.restore.novel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.moblink.demo.R;

public class NovelListAdapter extends BaseAdapter {

	private int[] icons = new int[]{R.drawable.moblink_demo_novel_icon0, R.drawable.moblink_demo_novel_icon1, R.drawable.moblink_demo_novel_icon2};
	private int[] titles = new int[]{R.string.moblink_demo_novel_title0, R.string.moblink_demo_novel_title1, R.string.moblink_demo_novel_title2};
	private int[] authors = new int[]{R.string.moblink_demo_novel_author0, R.string.moblink_demo_novel_author1, R.string.moblink_demo_novel_author2};
	private int[] descIds = new int[]{R.string.moblink_demo_novel_desc0, R.string.moblink_demo_novel_desc1, R.string.moblink_demo_novel_desc2};

	private Context context;

	public NovelListAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.moblink_demo_layout_novel_item, parent, false);
		}
		ImageView icon = (ImageView) convertView.findViewById(R.id.moblink_demo_novelIv);
		TextView name = (TextView) convertView.findViewById(R.id.moblink_demo_novelNameTv);
		TextView author = (TextView) convertView.findViewById(R.id.moblink_demo_novelAuthorTv);
		TextView desc = (TextView) convertView.findViewById(R.id.moblink_demo_novelDescTv);

		icon.setImageResource(icons[position]);
		name.setText(titles[position]);
		author.setText(authors[position]);
		desc.setText(descIds[position]);
		return convertView;
	}
}
