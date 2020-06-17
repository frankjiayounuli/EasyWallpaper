package com.pengxh.easywallpaper.ui

import android.graphics.Color
import android.util.Log
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.BannerImageAdapter
import com.pengxh.easywallpaper.bean.BannerBean
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
        }
    }

    private fun initBanner(bannerList: ArrayList<BannerBean>) {
        //轮播图
        val bannerImageAdapter = BannerImageAdapter(context, bannerList)
        discoverBanner.addBannerLifecycleObserver(this)
            .setAdapter(bannerImageAdapter)
            .setIndicator(CircleIndicator(context))
            .start()
        bannerImageAdapter.setOnItemClickListener(object :
            OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                //查看大图
//                showBigPicture(bannerBeanList[position].bannerImage)
            }
        })
    }
}