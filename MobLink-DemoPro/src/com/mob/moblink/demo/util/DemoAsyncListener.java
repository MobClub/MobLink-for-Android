package com.mob.moblink.demo.util;

public interface DemoAsyncListener<T> {
	void onSuccess(T t);
	void onFailure(int responseCode, Throwable e);
}
