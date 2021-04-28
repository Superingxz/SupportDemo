package os.imlive.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import os.imlive.base.data.model.manager.UserManager
import os.imlive.base.http.response.LoginResponse
import os.imlive.base.widget.dialog.CommDialog
import os.imlive.common.ui.wallet.dialog.RechargeDialog
import os.imlive.demo.databinding.DemoBinding
import os.imlive.sdk.FloatLiveManager
import os.imlive.sdk.util.FloatLiveTools

class DemoKtActivity : AppCompatActivity() {
    private lateinit var mBinding: DemoBinding
    private var commDialog: CommDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DemoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViews()
    }

    private fun initViews() {
        mBinding.tokenEt.setText(UserManager.getInstance().myThirdToken)
        if (UserManager.getInstance().thirdUid != 0L) {
            mBinding.thirdUidEt.setText(UserManager.getInstance().thirdUid.toString())
        }
        if (UserManager.getInstance().thirdTid != 0L) {
            mBinding.thirdTidEt.setText(UserManager.getInstance().thirdTid.toString())
        }
        mBinding.backImg.setOnClickListener {
            finish()
        }
        mBinding.login.setOnClickListener {
            if (TextUtils.isEmpty(mBinding.tokenEt.text.toString())) {
                ToastKit.show(this, R.string.sdk_input_token)
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mBinding.thirdUidEt.text.toString())) {
                ToastKit.show(this, R.string.sdk_input_user_id)
                return@setOnClickListener
            }
            FloatLiveManager.getInstance()
                .getLoginInstance(this)
                .setLoginListener { state, msg ->
                    when (state) {
                        LoginResponse.FAILED -> { // 登陆失败
                            ToastKit.show(this, msg)
                        }
                        LoginResponse.AUTH_FAILED -> { // 授权失败
                            ToastKit.show(this, R.string.sdk_string_auth_failed)
                        }
                        LoginResponse.AUTH_CANCEL -> { // 授权取消
                            ToastKit.show(this, R.string.sdk_string_auth_cancel)
                        }
                        LoginResponse.NEED_REGISTER -> { // 未注册
                            ToastKit.show(this, R.string.sdk_string_need_register)
                        }
                        LoginResponse.TOKEN_AUTH_FAILED -> { // token校验失效
                        }
                    }
                }
                .auth(
                    this,
                    mBinding.tokenEt.text.toString(),
                    mBinding.thirdUidEt.text.toString().toLong()
                )
        }
        mBinding.clearCacheTv.setOnClickListener {
            if (commDialog == null) {
                commDialog = CommDialog(this)
            }
            commDialog?.showDialogComm(
                {
                    FloatLiveTools.clearCache()
                    ToastKit.show(this, R.string.clear_cache_success)
                    commDialog?.dismiss()
                },
                R.string.clear_cache_confirm, null, R.string.cancel, R.string.sure, R.string.remind
            )
        }
        FloatLiveManager.getInstance().setRechargeCallback { context ->
            RechargeDialog().show(context.supportFragmentManager, "rechargeDialog")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FloatLiveManager.getInstance().unregister()
    }
}
