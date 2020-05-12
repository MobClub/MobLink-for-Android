package com.mob.moblink.demo.analyze;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.demo.BaseActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.model.AnalyzeData;
import com.mob.moblink.demo.model.AnalyzeRecord;
import com.mob.moblink.demo.util.DemoAsyncListener;
import com.mob.moblink.demo.util.DemoAsyncProtocol;

import java.util.ArrayList;

public class AnalyzeDetailActivity extends BaseActivity {

	public static final int LOCAL_INVITE_ANALYZE_TYPE = 1;
	public static final int SHARE_INVITE_ANALYZE_TYPE = 2;
	public static final int RESTORE_ANALYZE_TYPE = 3;

	private int analyzeType;
	private ListView listView;
	private TextView sourceTv;

	@Override
	protected int getTitleId() {
		switch (analyzeType) {
			case LOCAL_INVITE_ANALYZE_TYPE:
				return R.string.moblink_demo_local_analy_title;
			case SHARE_INVITE_ANALYZE_TYPE:
				return R.string.moblink_demo_share_analy_title;
			case RESTORE_ANALYZE_TYPE:
				return R.string.moblink_demo_restore_analy_title;
		}
		return R.string.moblink_demo_local_analy_title;
	}

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_analyze_detail;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		analyzeType = getIntent().getIntExtra("analyzeType", LOCAL_INVITE_ANALYZE_TYPE);
		rightIv.setVisibility(View.INVISIBLE);
		listView = (ListView) findViewById(R.id.moblink_demo_analy_listview);
		sourceTv = (TextView) findViewById(R.id.moblink_demo_sourceTv);

		if (analyzeType == LOCAL_INVITE_ANALYZE_TYPE) {
			sourceTv.setVisibility(View.GONE);
		} else if (analyzeType == RESTORE_ANALYZE_TYPE) {
			sourceTv.setText(R.string.moblink_demo_restore_title);
		}

		getRecord();
	}

	private void getRecord() {
		DemoAsyncProtocol.record(analyzeType, new DemoAsyncListener<AnalyzeRecord>() {
			@Override
			public void onSuccess(AnalyzeRecord analyzeRecord) {
				ArrayList<AnalyzeData> data;
				if (null == analyzeRecord.getRes() || null == analyzeRecord.getRes().data) {
					data = new ArrayList<AnalyzeData>();
				} else {
					data = analyzeRecord.getRes().data;
				}
				listView.setAdapter(new AnalyzeDetailAdapter(AnalyzeDetailActivity.this, analyzeType, data));
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {
				Toast.makeText(AnalyzeDetailActivity.this, "responseCode : " + responseCode + " error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
