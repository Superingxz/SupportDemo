package os.imlive.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.widget.TextView;

import os.imlive.base.config.FloatLiveEventType;
import os.imlive.base.http.response.LoginResponse;
import os.imlive.base.widget.dialog.CommDialog;
import os.imlive.sdk.FloatLiveManager;
import os.imlive.sdk.util.FloatLiveTools;

/**
 * <pre>
 *     author: myz
 *     email : moyaozhi@imdawei.com
 *     time  : 2021/4/12 12:16
 *     desc  : SDKDemo
 * </pre>
 */
public class DemoActivity extends AppCompatActivity {
    private CommDialog commDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        initViews();
    }

    private void initViews() {
        AppCompatImageView backImg = findViewById(R.id.backImg);
        AppCompatEditText tokenEt = findViewById(R.id.tokenEt);
        AppCompatEditText thirdUidEt = findViewById(R.id.thirdUidEt);
        TextView loginTv = findViewById(R.id.login);
        AppCompatTextView clearCacheTv = findViewById(R.id.clearCacheTv);
        backImg.setOnClickListener(v -> {
            finish();
        });
        loginTv.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tokenEt.getText().toString())) {
                ToastKit.show(this, R.string.sdk_input_token);
                return;
            }
            if (TextUtils.isEmpty(thirdUidEt.getText().toString())) {
                ToastKit.show(this, R.string.sdk_input_user_id);
                return;
            }
            FloatLiveManager.getInstance()
                    .getLoginInstance(this)
                    .setLoginListener((state, msg) -> {
                        switch (state) {
                            case LoginResponse.FAILED: // 登陆失败
                                ToastKit.show(DemoActivity.this, msg);
                                break;
                            case LoginResponse.AUTH_FAILED: // 授权失败
                                ToastKit.show(DemoActivity.this, R.string.sdk_string_auth_failed);
                                break;
                            case LoginResponse.AUTH_CANCEL: // 授权取消
                                ToastKit.show(DemoActivity.this, R.string.sdk_string_auth_cancel);
                                break;
                            case LoginResponse.NEED_REGISTER: // 未注册
                                ToastKit.show(DemoActivity.this, R.string.sdk_string_need_register);
                                break;
                            case LoginResponse.TOKEN_AUTH_FAILED: // token校验失效
                                break;
                        }
                    })
                    .auth(this, tokenEt.getText().toString(), Long.parseLong(thirdUidEt.getText().toString()));
        });
        clearCacheTv.setOnClickListener(v -> {
            if (commDialog == null) {
                commDialog = new CommDialog(this);
            }
            commDialog.showDialogComm(view -> {
                FloatLiveTools.clearCache();
                ToastKit.show(this, R.string.clear_cache_success);
                commDialog.dismiss();
            }, R.string.clear_cache_confirm, null, R.string.cancel, R.string.sure, R.string.remind);
        });
        FloatLiveManager.getInstance().setFloatLiveListener(type -> {
            switch (type) {
                case FloatLiveEventType.FLOAT_LIVE_TYPE_RECHARGE:
                    ToastKit.show(this, "调起充值页面");
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatLiveManager.getInstance().unregister();
    }
}
