package com.pengxh.easywallpaper.ui

import android.content.pm.ActivityInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
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
 * @description: TODO
 * @date: 2020/3/6 13:46
 */
class BigPictureActivity : BaseNormalActivity() {

    companion object {
        private const val Tag = "BigPictureActivity"
    }

    override fun initLayoutView(): Int {
        return R.layout.activity_big_picture
    }

    override fun initData() {
        ImmersionBar.with(this).init()
        val imageURL = intent.getStringExtra("imageURL")
        if (imageURL != null || imageURL != "") {
            GlobalScope.launch(Dispatchers.Main) {
                val drawable = withContext(Dispatchers.IO) {
                    Glide.with(this@BigPictureActivity).load(imageURL)
                        .apply(RequestOptions().centerCrop()).into(
                            Target.SIZE_ORIGINAL,
                            Target.SIZE_ORIGINAL
                        ).get()
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