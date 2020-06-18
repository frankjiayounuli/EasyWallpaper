package com.pengxh.easywallpaper.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.BigPictureAdapter
import com.pengxh.easywallpaper.utils.DocumentParseUtil
import kotlinx.android.synthetic.main.activity_wallpaper.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.*


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

    private val context: Context = this@WallpaperActivity
    private var wallpaperData: ArrayList<String> = ArrayList()

    override fun initLayoutView(): Int {
        return R.layout.activity_wallpaper
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        ImmersionBar.with(this).init()

        val wallpaperURL = intent.getStringExtra("wallpaperURL")!!
        Log.d(Tag, "大图链接地址: $wallpaperURL")
        GlobalScope.launch(Dispatchers.Main) {
            val document = withContext(Dispatchers.IO) {
                Jsoup.connect(wallpaperURL).timeout(10 * 1000).get()
            }
            wallpaperData = DocumentParseUtil.parseWallpaperData(document)
            val bigPictureAdapter = BigPictureAdapter(context, wallpaperData)
            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            pictureGalleryView.layoutManager = linearLayoutManager
            pictureGalleryView.adapter = bigPictureAdapter
        }
    }

    override fun initEvent() {
        val snackbar = Snackbar.make(coordinatorLayout, "长按图片可以保存高清壁纸哦~", Snackbar.LENGTH_LONG)
        snackbar.setAction("知道了") { snackbar.dismiss() }
        snackbar.show()
    }
}