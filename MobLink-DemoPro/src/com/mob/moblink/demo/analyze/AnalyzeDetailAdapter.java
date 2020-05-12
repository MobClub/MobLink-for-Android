package com.mob.moblink.demo.analyze;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mob.moblink.demo.R;
import com.mob.moblink.demo.model.AnalyzeData;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

import static com.mob.moblink.demo.analyze.AnalyzeDetailActivity.RESTORE_ANALYZE_TYPE;
import static com.mob.moblink.demo.analyze.AnalyzeDetailActivity.SHARE_INVITE_ANALYZE_TYPE;

public class AnalyzeDetailAdapter extends BaseAdapter {

	private int analyzeType;
	private Context context;
	private ArrayList<AnalyzeData> data;

	public AnalyzeDetailAdapter(Context context, int analyzeType, ArrayList<AnalyzeData> data) {
		this.context = context;
		this.analyzeType = analyzeType;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public AnalyzeData getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.moblink_demo_layout_analyze_item, parent, false);
		}
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.color.moblink_demo_bg);
		} else {
			convertView.setBackgroundColor(Color.WHITE);
		}
		TextView sourceTv = (TextView) convertView.findViewById(R.id.moblink_demo_sourceTv);
		TextView posTv = (TextView) convertView.findViewById(R.id.moblink_demo_posTv);
		TextView dateTv = (TextView) convertView.findViewById(R.id.moblink_demo_dateTv);
		TextView numTv = (TextView) convertView.findViewById(R.id.moblink_demo_numTv);

		AnalyzeData data = getItem(position);
		posTv.setText((position+1) + "");
		dateTv.setText(data.date);
		numTv.setText(data.num+"");
		if (analyzeType == SHARE_INVITE_ANALYZE_TYPE && null != data.channel) {
			int resid = ResHelper.getStringRes(context, "moblink_demo_channel_" + data.channel);
			if (resid > 0) {
				sourceTv.setText(resid);
			}
		} else if (analyzeType == RESTORE_ANALYZE_TYPE && null != data.scene) {
			int resid = ResHelper.getStringRes(context, "moblink_demo_scene_" + data.scene);
			if (resid > 0) {
				sourceTv.setText(resid);
			}
		} else {
			sourceTv.setVisibility(View.GONE);
		}

		return convertView;
	}

}
