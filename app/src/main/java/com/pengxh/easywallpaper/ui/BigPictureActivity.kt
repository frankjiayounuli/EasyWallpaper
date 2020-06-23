package com.pengxh.easywallpaper.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import kotlinx.android.synthetic.main.activity_big_picture.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO 查看大图
 * @date: 2020/3/6 13:46
 */
class BigPictureActivity : BaseNormalActivity() {

    companion object {
        private const val Tag = "BigPictureActivity"
    }

    private val context: Context = this@BigPictureActivity

    override fun initLayoutView(): Int = R.layout.activity_big_picture

    override fun initData() {
        ImmersionBar.with(this).init()
        val imageURI = intent.getStringExtra("imageURI")
        if (imageURI != null || imageURI != "") {
            GlobalScope.launch(Dispatchers.Main) {
                val drawable = withContext(Dispatchers.IO) {
                    Drawable.createFromStream(
                        contentResolver.openInputStream(Uri.parse(imageURI)),
                        null
                    )
                }
                val width = drawable.intrinsicWidth
                val height = drawable.intrinsicHeight
                requestedOrientation = if (width > height) {
                    //横屏显示
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    //竖屏显示
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                photoView.background = drawable
            }
        }
    }

    override fun initEvent() {

    }
}