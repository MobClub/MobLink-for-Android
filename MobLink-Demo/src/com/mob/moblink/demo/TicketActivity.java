package com.mob.moblink.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.MobLink;
import com.mob.moblink.demo.util.CommonUtils;

public class TicketActivity extends BaseActivity {

	private TextView tvShare;
	private Spinner spFlyFrom;
	private Spinner spFlyTo;
	private Button btnDate3;
	private Button btnDate4;
	private Button btnDate5;
	private Button btnSearch;
	private String flyFrom;
	private String flyTo;
	private String flyDate;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ticket);

		tvShare = (TextView) findViewById(R.id.tv_share);
		spFlyFrom = (Spinner) findViewById(R.id.sp_fly_from);
		spFlyTo = (Spinner) findViewById(R.id.sp_fly_to);
		btnDate3 = (Button) findViewById(R.id.btn_date3);
		btnDate4 = (Button) findViewById(R.id.btn_date4);
		btnDate5 = (Button) findViewById(R.id.btn_date5);
		btnSearch = (Button) findViewById(R.id.btn_search);

		tvShare.setOnClickListener(this);
		btnDate3.setOnClickListener(this);
		btnDate4.setOnClickListener(this);
		btnDate5.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

		btnDate3.setSelected(true);
		flyDate = btnDate3.getText().toString();
		spFlyTo.setSelection(1);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_share: {
				//获取mobid后再分享
				flyFrom = spFlyFrom.getSelectedItem().toString();
				flyTo = spFlyTo.getSelectedItem().toString();
				if (flyFrom.equals(flyTo)) {
					Toast.makeText(this, R.string.fly_select_city_error, Toast.LENGTH_SHORT).show();
					return;
				}
				share();
			} break;
			case R.id.btn_date3: {
				btnDate3.setSelected(true);
				btnDate4.setSelected(false);
				btnDate5.setSelected(false);
				flyDate = btnDate3.getText().toString();
			} break;
			case R.id.btn_date4: {
				btnDate3.setSelected(false);
				btnDate4.setSelected(true);
				btnDate5.setSelected(false);
				flyDate = btnDate4.getText().toString();
			} break;
			case R.id.btn_date5: {
				btnDate3.setSelected(false);
				btnDate4.setSelected(false);
				btnDate5.setSelected(true);
				flyDate = btnDate5.getText().toString();
			} break;
			case R.id.btn_search: {
				flyFrom = spFlyFrom.getSelectedItem().toString();
				flyTo = spFlyTo.getSelectedItem().toString();
				if (flyFrom.equals(flyTo)) {
					Toast.makeText(this, R.string.fly_select_city_error, Toast.LENGTH_SHORT).show();
					return;
				}
				Intent i = new Intent(this, TicketDetailActivity.class);
				i.putExtra("flyFrom", flyFrom);
				i.putExtra("flyTo", flyTo);
				i.putExtra("flyDate", flyDate);
				startActivity(i);
			} break;
			default: {
				super.onClick(v);
			} break;
		}
	}

	private void share() {
		String shareUrl = CommonUtils.getShareUrl() + CommonUtils.TICKET_PATH;
		String title = getString(R.string.ticket_share_titel);
		String text = getString(R.string.share_text);
		String imgPath = CommonUtils.copyImgToSD(this, R.drawable.demo_share_ticket , "ticket");
		CommonUtils.showShare(this, title, text, shareUrl, imgPath);
	}

}
