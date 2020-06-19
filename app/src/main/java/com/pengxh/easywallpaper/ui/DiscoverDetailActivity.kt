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
import com.pengxh.easywallpaper.utils.*
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_discover_detail.*
import kotlinx.android.synthetic.main.include_title.*
import org.jsoup.nodes.Document

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
        HttpHelper.getDocumentData(discoverURL!!, object : HttpListener {
            override fun onSuccess(result: Document) {
                //解析Banner数据
                initBanner(HTMLParseUtil.parseBannerData(result))

                //解析探索发现数据
                initDiscover(HTMLParseUtil.parseDiscoverDetailData(result))
            }

            override fun onFailure(e: Exception) {

            }
        })
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
                        startWallpaperActivity(wallpaperURL)
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
                startWallpaperActivity(bannerList[position].bannerLink)
            }
        })
    }

    private fun startWallpaperActivity(link: String) {
        Log.d(Tag, "大图集合链接: $link")
        val intent = Intent(context, WallpaperActivity::class.java)
        intent.putExtra("wallpaperURL", link)
        startActivity(intent)
    }
}