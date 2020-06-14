package com.pengxh.easywallpaper.ui

import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.DocumentParseUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

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

    override fun initLayoutView(): Int {
        return R.layout.activity_wallpaper
    }

    override fun initData() {
        ImmersionBar.with(this).init()

        val wallpaperURL = intent.getStringExtra("wallpaperURL")!!
        GlobalScope.launch(Dispatchers.Main) {
            val document = withContext(Dispatchers.IO) {
                Jsoup.connect(wallpaperURL).timeout(10 * 1000).get()
            }
            //TODO 未完待续...
            DocumentParseUtil.parseWallpaperData(document)
        }
    }

    override fun initEvent() {

    }
}