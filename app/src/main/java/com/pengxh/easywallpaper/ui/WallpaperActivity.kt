package com.pengxh.easywallpaper.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar
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

    override fun initLayoutView(): Int = R.layout.activity_wallpaper

    override fun initData() {
        ImmersionBar.with(this).init()

        val wallpaperURL = intent.getStringExtra("wallpaperURL")!!
        HttpHelper.getDocumentData(wallpaperURL, object : HttpListener {
            override fun onSuccess(result: Document) {
                val wallpaperData = HTMLParseUtil.parseWallpaperData(result)

                /**
                 * 绑定大图画廊
                 * 1、RecycleView方式实现，滑动阻尼较大，不太合适
                 *
                 * 2、ViewPage+Fragment实现
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

    private class PicturePagerAdapter internal constructor(
        fm: FragmentManager?,
        private val pageList: List<Fragment>
    ) :
        FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return pageList[position]
        }

        override fun getCount(): Int {
            return pageList.size
        }
    }
}