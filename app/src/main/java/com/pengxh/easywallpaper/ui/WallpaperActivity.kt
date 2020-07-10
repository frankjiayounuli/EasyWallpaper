package com.pengxh.easywallpaper.ui

import com.google.android.material.snackbar.Snackbar
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.PicturePagerAdapter
import com.pengxh.easywallpaper.ui.fragment.BigPictureFragment.Companion.newInstance
import com.pengxh.easywallpaper.utils.HTMLParseUtil
import com.pengxh.easywallpaper.utils.HttpHelper
import com.pengxh.easywallpaper.utils.HttpListener
import kotlinx.android.synthetic.main.activity_wallpaper.*
import org.jsoup.nodes.Document


/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/14 23:32
 */
class WallpaperActivity : BaseNormalActivity() {

    companion object {
        private const val Tag: String = "WallpaperActivity"
    }

    override fun initLayoutView(): Int = R.layout.activity_wallpaper

    override fun initData() {
        ImmersionBar.with(this).init()

        val wallpaperURL = intent.getStringExtra("wallpaperURL")!!

        val bigPictureAdapter = PicturePagerAdapter(supportFragmentManager)
        HttpHelper.getDocumentData(wallpaperURL, object : HttpListener {
            override fun onSuccess(result: Document) {
                val wallpaperData = HTMLParseUtil.parseWallpaperData(result)
                /**
                 * 绑定大图画廊
                 * 1、RecycleView方式实现，滑动阻尼较大，不太合适
                 *
                 * 2、ViewPage+Fragment实现
                 * */
                wallpaperData.forEach {
                    newInstance(it)?.let { fragment -> bigPictureAdapter.addPage(fragment) }
                }
                bigPictureViewPager.adapter = bigPictureAdapter
            }

            override fun onFailure(e: Exception) {
                e.printStackTrace()
            }
        })
    }

    override fun initEvent() {
        val snackBar = Snackbar.make(coordinatorLayout, "长按图片可以保存高清壁纸哦~", Snackbar.LENGTH_LONG)
        snackBar.setAction("知道了") { snackBar.dismiss() }
        snackBar.show()
    }
}