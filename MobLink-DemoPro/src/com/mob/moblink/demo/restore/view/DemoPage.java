package com.mob.moblink.demo.restore.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.moblink.demo.MainActivity;
import com.mob.moblink.demo.R;
import com.mob.moblink.demo.restore.presenter.RestorePresenter;
import com.mob.moblink.demo.util.CommonUtils;
import com.mob.moblink.demo.common.ShareHelper;

import java.util.HashMap;

public class DemoPage implements View.OnClickListener, IRestoreView {
	private MainActivity mainActivity;
	private View rootView;
	private Button btnShare;
	private String mobID;
	private RestorePresenter restorePresenter;

	public DemoPage(View view, MainActivity mainActivity) {
		super();
		rootView = view;
		this.mainActivity = mainActivity;
		initView();
		restorePresenter = new RestorePresenter(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_get_mobid: {
				//获取MobID
				String path;
				Spinner spPath = (Spinner) findViewById(R.id.sp_path);
				if (null != spPath) {
					int selectedID = spPath.getSelectedItemPosition();
					path = CommonUtils.MAIN_PATH_ARR[selectedID];
				} else {
					path = mainActivity.getString(R.string.path_wx_mini);
				}
				restorePresenter.getMobId(path, collectParams());
				break;
			}
			case R.id.btn_share: {
				if (TextUtils.isEmpty(mobID)) {
					ShareHelper.noMobIdWarning(mainActivity).show();
					return;
				}
				//演示界面的分享功能
				String shareUrl = CommonUtils.getAppLinkShareUrl() + "/" + mobID;
				String title = mainActivity.getString(R.string.show_share_titel);
				String text = mainActivity.getString(R.string.share_text);
				String imgPath = CommonUtils.copyImgToSD(mainActivity, R.drawable.demo_share_moblink, "moblink");
				restorePresenter.share(mainActivity, title, text, shareUrl,mobID, imgPath);
			} break;
			case R.id.btn_share_to_wxmini: {
				if (TextUtils.isEmpty(mobID)) {
					ShareHelper.noMobIdWarning(mainActivity).show();
					return;
				}
				CommonUtils.showShareWxMini(mainActivity, mobID);
			} break;
			case R.id.tv_set_default_value: {
				//填充默认值
				final int[] tvKeys = new int[]{
						R.id.et_key1,
						R.id.et_key2,
						R.id.et_key3
				};
				final int[] tvValues = new int[] {
						R.id.et_value1,
						R.id.et_value2,
						R.id.et_value3
				};
				final int[] keys = new int[] {
						R.string.key1,
						R.string.key2,
						R.string.key3
				};
				final int[] values = new int[] {
						R.string.value1,
						R.string.value2,
						R.string.value3
				};

				for (int i = 0; i < tvKeys.length; i++) {
					TextView tv = (TextView)findViewById(tvKeys[i]);
					tv.setText(keys[i]);
					tv = (TextView)findViewById(tvValues[i]);
					tv.setText(values[i]);
				}
			} break;
		}
	}

	@Override
	public void onMobIdGot(String mobId) {
		this.btnShare.setSelected(true);
		DemoPage.this.mobID = mobId;
		Toast.makeText(mainActivity, "Get MobID Successfully!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMobIdError(Throwable t) {
		btnShare.setSelected(true);
		Toast.makeText(mainActivity, "Get MobID Failed!", Toast.LENGTH_SHORT).show();
	}

	private void initView() {
		findViewById(R.id.btn_get_mobid).setOnClickListener(this);
		findViewById(R.id.tv_set_default_value).setOnClickListener(this);

		btnShare = (Button) findViewById(R.id.btn_share);
		if (null == btnShare) {
			btnShare = (Button) findViewById(R.id.btn_share_to_wxmini);
		}

		btnShare.setSelected(false);
		btnShare.setOnClickListener(this);
	}

	private View findViewById(int id) {
		return rootView.findViewById(id);
	}

	private HashMap<String, Object> collectParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		final int[] keys = new int[]{
				R.id.et_key1,
				R.id.et_key2,
				R.id.et_key3
		};
		final int[] values = new int[] {
				R.id.et_value1,
				R.id.et_value2,
				R.id.et_value3
		};

		for (int i = 0; i < keys.length; i++) {
			TextView tvKey = (TextView)findViewById(keys[i]);
			TextView tvValue = (TextView)findViewById(values[i]);
			params.put(tvKey.getText().toString(), tvValue.getText().toString());
		}
		return params;
	}
}
