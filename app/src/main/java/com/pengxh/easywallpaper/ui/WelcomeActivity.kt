package com.pengxh.easywallpaper.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pengxh.app.multilib.widget.dialog.PermissionDialog
import com.pengxh.easywallpaper.R
import pub.devrel.easypermissions.EasyPermissions

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 14:21
 */
class WelcomeActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    companion object {
        private const val PERMISSIONS_CODE = 999
        private val USER_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //*表示可变参数，比如Java中的 String... args
            if (EasyPermissions.hasPermissions(this, *USER_PERMISSIONS)) {
                startMainActivity()
            } else {
                PermissionDialog.Builder().setContext(this).setPermission(USER_PERMISSIONS)
                    .setOnDialogClickListener(object : PermissionDialog.onDialogClickListener {
                        override fun onButtonClick() {
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
            startMainActivity()
        }
    }

    private fun startMainActivity() {
//        startActivity(Intent(this, TestActivity::class.java))
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startMainActivity()
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