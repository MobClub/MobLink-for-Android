package com.mob.moblink.demo;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.moblink.Scene;
import com.mob.moblink.SceneRestorable;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketDetailActivity extends BaseActivity implements SceneRestorable {

	private TextView tvTitle;
	private ListView lvTicketList;
	private String flyFrom;
	private String flyTo;
	private String flyDate;

	private String source;
	private MyAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ticket_detail);

		tvTitle = (TextView) findViewById(R.id.tv_title);
		lvTicketList = (ListView) findViewById(R.id.lv_ticket);

		flyFrom = getIntent().getStringExtra("flyFrom");
		flyTo = getIntent().getStringExtra("flyTo");
		flyDate = getIntent().getStringExtra("flyDate");

		if (!TextUtils.isEmpty(flyFrom) && !TextUtils.isEmpty(flyTo) && !TextUtils.isEmpty(flyDate)) {
			if (adapter == null) {
				adapter = new MyAdapter(this);
				adapter.setTicketsData(CommonUtils.getTicketsDate(this));
				lvTicketList.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		}

		tvTitle.setText(flyDate);
	}

	private class MyAdapter extends BaseAdapter {

		private Context context;
		private LayoutInflater inflater;
		private ArrayList<HashMap<String, Object>> ticketsData;

		public MyAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		public void setTicketsData(ArrayList<HashMap<String, Object>> ticketsData) {
			this.ticketsData = ticketsData;
		}

		public int getCount() {
			return ticketsData.size();
		}

		public Object getItem(int position) {
			if (ticketsData != null) {
				ticketsData.get(position);
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
				convertView = inflater.inflate(R.layout.layout_ticket_item, null);
				holder.tvFlyFromTime = (TextView) convertView.findViewById(R.id.tv_fly_from_time);
				holder.tvFlyToTime = (TextView) convertView.findViewById(R.id.tv_fly_to_time);
				holder.tvAirportFrom = (TextView) convertView.findViewById(R.id.tv_airport_from);
				holder.tvAirportTo = (TextView) convertView.findViewById(R.id.tv_airport_to);
				holder.tvTicketPrice = (TextView) convertView.findViewById(R.id.tv_ticket_price);
				holder.tvTicketDiscount = (TextView) convertView.findViewById(R.id.tv_ticket_discount);
				holder.tvPlaneName = (TextView) convertView.findViewById(R.id.tv_plane_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvFlyFromTime.setText((String)ticketsData.get(position).get("fromTime"));
			holder.tvFlyToTime.setText((String)ticketsData.get(position).get("ToTime"));
			holder.tvAirportFrom.setText(flyFrom);
			holder.tvAirportTo.setText(flyTo);
			holder.tvTicketPrice.setText((String)ticketsData.get(position).get("ticketPrice"));
			holder.tvTicketDiscount.setText((String)ticketsData.get(position).get("ticketDiscount"));
			holder.tvPlaneName.setText((String)ticketsData.get(position).get("planeName"));

			return convertView;
		}

		private class ViewHolder {
			public TextView tvFlyFromTime;
			public TextView tvFlyToTime;
			public TextView tvAirportFrom;
			public TextView tvAirportTo;
			public TextView tvTicketPrice;
			public TextView tvTicketDiscount;
			public TextView tvPlaneName;
		}
	}

	@Override
	public void onReturnSceneData(Scene scene) {
		if (scene != null) {
			path = scene.path;
			source = scene.source;
			paramStr = "";
			if (scene.params != null) {
				for (Map.Entry<String, Object> entry : scene.params.entrySet()) {
					paramStr += (entry.getKey() + " : " + entry.getValue() + "\r\n");
				}
			}

			// dialog不能复用, 防止参数更换, 不能及时更新
			if (null != dialog && dialog.isShowing()) {
				dialog.dismiss();
			}
			dialog = CommonUtils.getDialog(this, scene.path, scene.source, paramStr);
			if (!dialog.isShowing()) {
				dialog.show();
			}
			if (null != scene.params) {
				if (scene.params.containsKey("from")) {
					flyFrom = String.valueOf(scene.params.get("from"));
				}
				if (scene.params.containsKey("to")) {
					flyTo = String.valueOf(scene.params.get("to"));
				}

				if (scene.params.containsKey("date")) {
					flyDate = String.valueOf(scene.params.get("date"));
					tvTitle.setText(flyDate);
				}
			}

			if (adapter == null) {
				adapter = new MyAdapter(this);
				adapter.setTicketsData(CommonUtils.getTicketsDate(this));
				lvTicketList.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		}
	}
}
