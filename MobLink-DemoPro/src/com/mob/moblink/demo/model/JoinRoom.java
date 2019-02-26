package com.mob.moblink.demo.model;

import java.io.Serializable;
import java.util.ArrayList;

public class JoinRoom extends BaseData<JoinRoom> implements Serializable {

	public ArrayList<DemoUser> user;
	public int roomId;
}
