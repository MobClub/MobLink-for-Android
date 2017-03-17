package com.mob.moblink.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.demo.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketDetailActivity extends Activity implements View.OnClickListener{

	private ImageView ivBack;
	private TextView tvTitle;
	private ListView lvTicketList;
	private String flyFrom;
	private String flyTo;
	private String flyDate;

	private Dialog dialog;
	private String source;
	private MyAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ticket_detail);

		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		lvTicketList = (ListView) findViewById(R.id.lv_ticket);

		flyFrom = getIntent().getStringExtra("flyFrom");
		flyTo = getIntent().getStringExtra("flyTo");
		flyDate = getIntent().getStringExtra("flyDate");

		tvTitle.setText(flyDate);
		ivBack.setOnClickListener(this);
	}

	protected void onResume() {
		super.onResume();
		MobLink.initSDK(this, CommonUtils.APPKEY);
		MobLink.setIntentHandler(getIntent(), new ActionListener() {
			public void onResult(final HashMap<String, Object> res) {
				runOnUiThread(new Runnable() {
					public void run() {
						String paramStr = "";
						if (res != null) {
							if (res.get("source") != null) {
								source = String.valueOf(res.get("source"));
							}
							if (res.get("params") != null) {
								HashMap<String, Object> params = (HashMap<String, Object>) res.get("params");
								for (Map.Entry<String, Object> entry : params.entrySet()) {
									if ("from".equals(entry.getKey())) {
										flyFrom = (String) entry.getValue();
									} else if ("to".equals(entry.getKey())) {
										flyTo = (String) entry.getValue();
									} else if ("date".equals(entry.getKey())) {
										flyDate = (String) entry.getValue();
										tvTitle.setText(flyDate);
									}
									paramStr += (entry.getKey() + " : " + entry.getValue() + "\r\n");
								}
							}
						}

						adapter.notifyDataSetChanged();

						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						dialog = CommonUtils.getDialog(TicketDetailActivity.this, CommonUtils.TICKET_PATH, source, paramStr);
						dialog.show();
					}
				});
			}
			public void onError(Throwable t) {
				if (t != null) {
					Toast.makeText(TicketDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});

		if (adapter == null) {
			adapter = new MyAdapter(this);
			adapter.setTicketsData(CommonUtils.getTicketsDate(this));
			lvTicketList.setAdapter(adapter);
		}

		setIntent(null);
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back: {
				Intent i = new Intent(this, TicketActivity.class);
				i.putExtra("tag", 3);
				startActivity(i);
			} break;
			default:
			break;
		}
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

}
