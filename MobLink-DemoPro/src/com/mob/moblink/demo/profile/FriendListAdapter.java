package com.mob.moblink.demo.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mob.moblink.demo.R;
import com.mob.moblink.demo.model.DemoUser;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<DemoUser> friendList;
	private OnCancelFollowListener onCancelFollowListener;

	public FriendListAdapter(Context context, ArrayList<DemoUser> friendList) {
		this.context = context;
		this.friendList = friendList;
	}

	@Override
	public int getCount() {
		return friendList.size();
	}

	public ArrayList<DemoUser> getFriendList() {
		return friendList;
	}

	public void setOnCancelFollowListener(OnCancelFollowListener onCancelFollowListener) {
		this.onCancelFollowListener = onCancelFollowListener;
	}

	@Override
	public DemoUser getItem(int position) {
		return friendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.moblink_demo_layout_friend_item, parent, false);
		}

		AsyncImageView usericon = (AsyncImageView) convertView.findViewById(R.id.moblink_demo_userIv);
		TextView usernameTv = (TextView) convertView.findViewById(R.id.moblink_demo_usernameTv);
		Button followBtn = (Button) convertView.findViewById(R.id.moblink_demo_followBtn);
		followBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != onCancelFollowListener) {
					onCancelFollowListener.onCancel(position);
				}
			}
		});
		usericon.setRound(ResHelper.dipToPx(context, 360));

		final DemoUser user = getItem(position);
		usericon.execute(user.avatar, R.drawable.moblink_demo_default_user);
		usernameTv.setText(user.nickname);

		return convertView;
	}

	public interface OnCancelFollowListener{
		void onCancel(int position);
	}
}

