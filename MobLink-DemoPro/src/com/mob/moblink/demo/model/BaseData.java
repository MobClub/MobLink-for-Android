package com.mob.moblink.demo.model;

import java.io.Serializable;

public class BaseData<T> implements Serializable {
	public T res;
	public int success;

	public T getRes() {
		return res;
	}
}
