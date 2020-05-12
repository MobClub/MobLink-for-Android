package com.mob.moblink.demo;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ActivityA extends ShareableActivity {

	private TextView tvActTitle;
	private TextView tvActText;
	private Button btnCheckParams;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_a;
	}

	@Override
	protected int getTitleId() {
		return R.string.activity_a;
	}

	protected void onViewCreated(Bundle savedInstanceState) {
		tvActTitle = (TextView) findViewById(R.id.tv_act_title);
		tvActText = (TextView) findViewById(R.id.tv_act_text);
		btnCheckParams = (Button) findViewById(R.id.btn_check_params);

		tvActTitle.setText(R.string.activity_a);
		tvActText.setText(R.string.activity_text_a);

		btnCheckParams.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_check_params: {
				showParamDialog();
			} break;
			default: {
				super.onClick(v);
			} break;
		}
	}
}
