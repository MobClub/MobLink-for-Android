package com.mob.moblink.demo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendList extends BaseData<FriendList> implements Serializable {
	public ArrayList<DemoUser> user;
}
