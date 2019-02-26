package com.mob.moblink.demo.restore.novel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mob.moblink.demo.R;
import com.mob.moblink.demo.ShareableActivity;

public class NovelListActivity extends ShareableActivity {

	private ListView novelListView;

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_novel_list;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		rightIv.setVisibility(View.GONE);
		novelListView = (ListView) findViewById(R.id.moblink_demo_novelListView);
		novelListView.setAdapter(new NovelListAdapter(this));

		novelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(NovelListActivity.this, NovelReadActivity.class);
				intent.putExtra("novelId", position);
				startActivity(intent);
			}
		});
	}

	@Override
	protected int getTitleId() {
		return R.string.moblink_demo_novel_restore;
	}
}
