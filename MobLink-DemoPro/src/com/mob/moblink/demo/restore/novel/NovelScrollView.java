package com.mob.moblink.demo.restore.novel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class NovelScrollView extends ScrollView {
	private OnScrollListener listener;

	public NovelScrollView(Context context) {
		super(context);
	}

	public NovelScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NovelScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(listener != null){
			//这里我只传了垂直滑动的距离
			listener.onScroll(t);
		}
	}

	public void setListener(OnScrollListener listener) {
		this.listener = listener;
	}

	//设置接口
	public interface OnScrollListener{
		void onScroll(int scrollY);
	}
}
