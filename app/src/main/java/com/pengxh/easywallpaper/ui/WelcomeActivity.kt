package com.pengxh.easywallpaper.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.widgets.AgreementDialog
import pub.devrel.easypermissions.EasyPermissions


/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 14:21
 */
class WelcomeActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    companion object {
        private const val Tag = "WelcomeActivity"
        private const val PERMISSIONS_CODE = 999
        private val USER_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //*表示可变参数，比如Java中的 String... args
            if (EasyPermissions.hasPermissions(this, *USER_PERMISSIONS)) {
                startSplashActivity()
            } else {
                AgreementDialog.Builder()
                    .setContext(this)
                    .setDialogTitle("免责声明和隐私政策")
                    .setDialogMessage("我们将严格按照上述协议为您提供服务，保护您的信息安全，点击“同意”即表示您已阅读并同意全部条款，可以继续使用本应用。")
                    .setOnDialogClickListener(object :
                        AgreementDialog.OnDialogClickListener {
                        override fun onConfirmClick() {
                            EasyPermissions.requestPermissions(
                                this@WelcomeActivity,
                                resources.getString(R.string.app_name) + "需要获取存储相关权限",
                                PERMISSIONS_CODE,
                                *USER_PERMISSIONS
                            )
                        }

                        override fun onCancelClick() {
                            finish()
                        }
                    }).build().show()
            }
        } else {
            startSplashActivity()
        }
    }

    private fun startSplashActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        perms.forEach {
            Log.d(Tag, it)
        }
        startSplashActivity()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}