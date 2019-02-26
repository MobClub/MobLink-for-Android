package com.mob.moblink.demo.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.Scene;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.ShareableActivity;
import com.mob.moblink.demo.match.MatchActivity;
import com.mob.moblink.demo.model.DemoUser;
import com.mob.moblink.demo.model.FriendList;
import com.mob.moblink.demo.util.DemoAsyncListener;
import com.mob.moblink.demo.util.DemoAsyncProtocol;

public class FriendActivity extends ShareableActivity {

	private ListView listView;
	private TextView emptyTv;
	private FriendListAdapter adapter;
	private boolean hasScene;


	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_friend;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		rightIv.setVisibility(View.INVISIBLE);
		listView = (ListView) findViewById(R.id.moblink_demo_friendLv);
		emptyTv = (TextView) findViewById(R.id.moblink_demo_emptyTv);

		getFriendList();
	}

	@Override
	public void onReturnSceneData(Scene scene) {
		super.onReturnSceneData(scene);
		if (null != scene && null != scene.getParams()) {
			String addId = null;
			if (scene.getParams().containsKey("sourceId")) {
				addId = String.valueOf(scene.getParams().get("sourceId"));
			} else if (scene.getParams().containsKey("id")) {
				addId = String.valueOf(scene.getParams().get("id"));
			}
			if (null != addId) {
				Integer channel = null;
				if (scene.getParams().containsKey("channel")) {
					channel = Integer.valueOf(String.valueOf(scene.getParams().get("channel")));
				}
				hasScene = true;
				DemoAsyncProtocol.addFriend(addId, channel, new DemoAsyncListener<DemoUser>() {
					@Override
					public void onSuccess(DemoUser demoUser) {
					}

					@Override
					public void onFailure(int responseCode, Throwable e) {
					}
				});
			}

		}
	}

	private void getFriendList() {
		DemoAsyncProtocol.friendList(new DemoAsyncListener<FriendList>() {
			@Override
			public void onSuccess(FriendList friendList) {
				if (null == friendList || null == friendList.getRes() || null == friendList.getRes().user || friendList.getRes().user.size() <= 0) {
//					emptyTv.setVisibility(View.VISIBLE);
				} else {
					emptyTv.setVisibility(View.GONE);
					adapter = new FriendListAdapter(FriendActivity.this, friendList.getRes().user);
					listView.setAdapter(adapter);
					adapter.setOnCancelFollowListener(new FriendListAdapter.OnCancelFollowListener() {
						@Override
						public void onCancel(int position) {
							delFriend(adapter.getItem(position));
						}
					});
				}
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {
				Toast.makeText(FriendActivity.this, "responseCode : " + responseCode + " error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}


	private void delFriend(final DemoUser user) {
		DemoAsyncProtocol.delFriend(user.id, new DemoAsyncListener<DemoUser>() {
			@Override
			public void onSuccess(DemoUser demoUser) {
				adapter.getFriendList().remove(user);
				adapter.notifyDataSetChanged();
				if (adapter.getFriendList().size() <= 0) {
//					emptyTv.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFailure(int responseCode, Throwable e) {
				Toast.makeText(FriendActivity.this, "responseCode : " + responseCode + " error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected boolean onLeftEvent() {
		if (hasScene) {
			startActivity(new Intent(this, MatchActivity.class));
			finish();
			return true;
		}
		return super.onLeftEvent();
	}

	@Override
	public void onBackPressed() {
		if (hasScene) {
			startActivity(new Intent(this, MatchActivity.class));
			finish();
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected int getTitleId() {
		return R.string.moblink_demo_friends;
	}
}
