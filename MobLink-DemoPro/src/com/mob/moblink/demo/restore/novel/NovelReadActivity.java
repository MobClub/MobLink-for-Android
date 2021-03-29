package com.mob.moblink.demo.restore.novel;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.Scene;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.ShareableActivity;
import com.mob.moblink.demo.common.ShareHelper;
import com.mob.moblink.demo.restore.presenter.RestorePresenter;
import com.mob.moblink.demo.restore.view.IRestoreView;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.util.MLog;
import com.mob.moblink.demo.util.SPHelper;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;

public class NovelReadActivity extends ShareableActivity implements IRestoreView {
	private static final String TAG = "NovelReadActivity";
	private TextView novelTv;
	private NovelScrollView novelSv;
	private int novelId;
	private float readPercent; //进度
	private HashMap<Integer, String> mobIdCache; //mobID缓存
	private String mobID;
	private RestorePresenter restorePresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		novelId = getIntent().getIntExtra("novelId", 0);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.moblink_demo_activity_novel_read;
	}

	@Override
	protected void onViewCreated(Bundle savedInstanceState) {
		mobIdCache = new HashMap<Integer, String>();
		novelTv = (TextView) findViewById(R.id.moblink_demo_novelTv);
		novelSv = (NovelScrollView) findViewById(R.id.moblink_demo_novelSv);

		novelTv.setText(Html.fromHtml(getString(ResHelper.getStringRes(this, "moblink_demo_novel_" + novelId))));

		novelSv.post(new Runnable() {
			@Override
			public void run() {
				novelSv.scrollTo(0, (int) (novelTv.getHeight() * readPercent));
			}
		});

		restorePresenter = new RestorePresenter(this);
	}

	@Override
	public void onReturnSceneData(Scene scene) {
		super.onReturnSceneData(scene);
		if (null != scene && null != scene.getParams()) {
			if (scene.getParams().containsKey("novel")) {
				novelId = Integer.parseInt(String.valueOf(scene.getParams().get("novel")));
			}
//			if (scene.getParams().containsKey("section")) {//章节
//				int section = Integer.parseInt(String.valueOf(scene.getParams().get("section")));
//			}
			if (scene.getParams().containsKey("percent")) {
				String percent = String.valueOf(scene.getParams().get("percent")).replace("%", "");
				readPercent = Integer.parseInt(percent) / 100f;
			}

			MLog.d(TAG, "novelId = " + novelId + ", readPercent = " + readPercent);
			if(centerTv != null && novelTv != null && novelSv != null ) {
				centerTv.setText(getTitleId());
				novelTv.setText(Html.fromHtml(getString(ResHelper.getStringRes(this, "moblink_demo_novel_" + novelId))));
				novelSv.post(new Runnable() {
					@Override
					public void run() {
						novelSv.scrollTo(0, (int) (novelTv.getHeight() * readPercent));
					}
				});
			}

		}
	}

	@Override
	protected void onRightEvent() {
		getMobIDToShare();
	}

	private void getMobIDToShare() {
		if (mobIdCache.containsKey(novelId)) {
			mobID = String.valueOf(mobIdCache.get(novelId));
			if (!TextUtils.isEmpty(mobID)) {
				share();
				return;
			}
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("novel", novelId);
		params.put("id", SPHelper.getDemoUserId());
		params.put("scene", CommonUtils.SCENE_NOVEL);
		restorePresenter.getMobId(CommonUtils.NOVEL_PATH, params);
	}

	@Override
	protected int getTitleId() {
		return ResHelper.getStringRes(this, "moblink_demo_novel_title" + novelId);
	}

	@Override
	public void onMobIdGot(String mobId) {
		if (mobId != null) {
			mobID = mobId;
			mobIdCache.put(novelId, mobId);
		} else {
			Toast.makeText(NovelReadActivity.this, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
		}
		share();
	}

	@Override
	public void onMobIdError(Throwable t) {
		if (t != null) {
			Toast.makeText(NovelReadActivity.this, "error = " + t.getMessage(), Toast.LENGTH_SHORT).show();
		}
		share();
	}

	private void share() {
		int shareIconId = ResHelper.getBitmapRes(this, "moblink_demo_novel_icon" + novelId);
		String imgPath = CommonUtils.copyImgToSD(this, shareIconId, "novel_" + novelId);
		String title = getString(ResHelper.getStringRes(this, "moblink_demo_novel_title" + novelId));
		String text = getString(ResHelper.getStringRes(this, "moblink_demo_novel_desc" + novelId));
		String shareUrl = CommonUtils.getShareUrl() + CommonUtils.NOVEL_PATH + "?id=" + novelId;
		if (!TextUtils.isEmpty(mobID)) {
			shareUrl += "&mobid=" + mobID;
		}
		ShareHelper.showShareReal(this, title, text, shareUrl,mobID, imgPath);
	}
}
