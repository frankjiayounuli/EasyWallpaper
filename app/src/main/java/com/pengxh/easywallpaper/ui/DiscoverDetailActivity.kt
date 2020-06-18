package com.pengxh.easywallpaper.ui

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.BannerImageAdapter
import com.pengxh.easywallpaper.adapter.WallpaperAdapter
import com.pengxh.easywallpaper.bean.BannerBean
import com.pengxh.easywallpaper.bean.WallpaperBean
import com.pengxh.easywallpaper.utils.DocumentParseUtil
import com.pengxh.easywallpaper.utils.OnItemClickListener
import com.pengxh.easywallpaper.utils.StatusBarColorUtil
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_discover_detail.*
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/17 14:47
 */
class DiscoverDetailActivity : BaseNormalActivity() {

    companion object {
        private const val Tag = "DiscoverDetailActivity"
    }

    private val context = this@DiscoverDetailActivity

    override fun initLayoutView(): Int {
        return R.layout.activity_discover_detail
    }

    override fun initData() {
        StatusBarColorUtil.setColor(context, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = intent.getStringExtra("discoverTitle")
        mTitleRightView.visibility = View.GONE
    }

    override fun initEvent() {
        val discoverURL = intent.getStringExtra("discoverURL")
        Log.d(Tag, "发现页地址: $discoverURL")

        GlobalScope.launch(Dispatchers.Main) {
            val discoverDocument = withContext(Dispatchers.IO) {
                Jsoup.connect(discoverURL).timeout(10 * 1000).get()
            }
            //解析Banner数据
            initBanner(DocumentParseUtil.parseBannerData(discoverDocument))

            //解析探索发现数据
            initDiscover(DocumentParseUtil.parseDiscoverDetailData(discoverDocument))
        }
    }

    private fun initDiscover(discoverDetailList: ArrayList<WallpaperBean>) {
        if (discoverDetailList.size == 0) {
            Log.d(Tag, "探索发现没有数据")
            emptyLayout.visibility = View.VISIBLE
            dataLayout.visibility = View.GONE
        } else {
            emptyLayout.visibility = View.GONE
            dataLayout.visibility = View.VISIBLE
            val wallpaperAdapter = WallpaperAdapter(context, discoverDetailList)
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            detailRecyclerView.layoutManager = staggeredGridLayoutManager
            detailRecyclerView.adapter = wallpaperAdapter
            wallpaperAdapter.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClickListener(position: Int) {
                    //跳转相应的壁纸分类
                    val wallpaperURL = discoverDetailList[position].wallpaperURL
                    if (wallpaperURL == "") {
                        EasyToast.showToast("加载失败，请稍后重试", EasyToast.WARING)
                    } else {
                        val intent = Intent(context, WallpaperActivity::class.java)
                        intent.putExtra("wallpaperURL", wallpaperURL)
                        startActivity(intent)
                    }
                }
            })
        }
    }

    private fun initBanner(bannerList: ArrayList<BannerBean>) {
        //轮播图
        val bannerImageAdapter = BannerImageAdapter(context, bannerList)
        discoverBanner.addBannerLifecycleObserver(this)
            .setAdapter(bannerImageAdapter)
            .setIndicator(CircleIndicator(context))
            .start()
        bannerImageAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                val bannerLink = bannerList[position].bannerLink
                Log.d(Tag, "轮播图链接: $bannerLink")

            }
        })
    }
}