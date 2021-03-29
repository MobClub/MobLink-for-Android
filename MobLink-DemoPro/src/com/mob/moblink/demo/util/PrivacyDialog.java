package com.mob.moblink.demo.util;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.mob.PrivacyPolicy;
import com.mob.moblink.demo.R;

public class PrivacyDialog extends Activity implements View.OnClickListener{

    private Button btnOk;
    private Button btnCancel;
    private TextView showContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_dialog);
        initView();
    }

    private void initView() {
        btnOk = findViewById(R.id.ok);
        btnOk.setOnClickListener(this);

        btnCancel = findViewById(R.id.cancel);
        btnCancel.setOnClickListener(this);

        showContent = findViewById(R.id.show_content);

        queryPrivacy();
    }

    private void queryPrivacy() {

        // 异步方法
        MobSDK.getPrivacyPolicyAsync(MobSDK.POLICY_TYPE_URL, new PrivacyPolicy.OnPolicyListener() {
            @Override
            public void onComplete(PrivacyPolicy data) {
                if (data != null) {
                    // 富文本内容
                    String text = data.getContent();
                    if (showContent != null) {
                        showContent.setText(MobSDK.getContext().getResources().getString(R.string.privacy_content) + "\n" +
                                MobSDK.getContext().getResources().getString(R.string.privacy_details) +  Html.fromHtml(text));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 请求失败
                //Log.e(TAG, "隐私协议查询结果：失败 " + t);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok: {
                submitPrivacyGrantResult(true);
                finish();
            } break;
            case R.id.cancel: {
                submitPrivacyGrantResult(false);
                finish();
            } break;
            default:
                break;
        }
    }

    private void submitPrivacyGrantResult(boolean granted) {
        MobSDK.submitPolicyGrantResult(granted, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                Log.e("AndyOn", "隐私协议授权结果提交：成功 " + data);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("AndyOn", "隐私协议授权结果提交：失败: " + t);
            }
        });
    }

}
