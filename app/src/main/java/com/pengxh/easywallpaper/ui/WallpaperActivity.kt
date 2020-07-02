package com.pengxh.easywallpaper.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.ui.fragment.BigPictureFragment
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
        HttpHelper.getDocumentData(wallpaperURL, object : HttpListener {
            override fun onSuccess(result: Document) {
                val wallpaperData = HTMLParseUtil.parseWallpaperData(result)

                /**
                 * ["http://www.win4000.com/mobile_detail_170489.html",
                 * "http://www.win4000.com/mobile_detail_170489_2.html",
                 * "http://www.win4000.com/mobile_detail_170489_3.html",
                 * "http://www.win4000.com/mobile_detail_170489_4.html",
                 * "http://www.win4000.com/mobile_detail_170489_5.html",
                 * "http://www.win4000.com/mobile_detail_170489_6.html",
                 * "http://www.win4000.com/mobile_detail_170489_7.html",
                 * "http://www.win4000.com/mobile_detail_170489_8.html",
                 * "http://www.win4000.com/mobile_detail_170489_9.html"]
                 *
                 * TODO 是否可以考虑将这些链接里面的图片地址全部解析出来？
                 * */
                Log.d(Tag, "高清壁纸大图网址链接: " + Gson().toJson(wallpaperData))

                /**
                 * 绑定大图画廊
                 * 1、RecycleView方式实现，滑动阻尼较大，不太合适
                 *
                 * 2、ViewPage+Fragment实现，TODO 创建多个Fragment会导致各种空指针，先继续调试一段时间，如果无法解决，依旧采用1方法
                 * */
                val fragmentList = ArrayList<Fragment>()
                wallpaperData.forEach {
                    fragmentList.add(BigPictureFragment(it))
                }
                val bigPictureAdapter = PicturePagerAdapter(supportFragmentManager, fragmentList)
                bigPictureViewPager.adapter = bigPictureAdapter
            }

            override fun onFailure(e: Exception) {

            }
        })
    }

    override fun initEvent() {
        val snackbar = Snackbar.make(coordinatorLayout, "长按图片可以保存高清壁纸哦~", Snackbar.LENGTH_LONG)
        snackbar.setAction("知道了") { snackbar.dismiss() }
        snackbar.show()
    }

    private class PicturePagerAdapter internal constructor(fm: FragmentManager?, private val pageList: List<Fragment>) : FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return pageList[position]
        }

        override fun getCount(): Int {
            return pageList.size
        }
    }
}