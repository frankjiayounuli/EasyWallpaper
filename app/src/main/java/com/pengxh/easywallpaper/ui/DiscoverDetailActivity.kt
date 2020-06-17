package com.pengxh.easywallpaper.ui

import android.graphics.Color
import android.util.Log
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.StatusBarColorUtil
import kotlinx.android.synthetic.main.include_title.*

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
        //获取爬虫抓取的Banner数据
//        GlobalScope.launch(Dispatchers.Main) {
//            val documentData = withContext(Dispatchers.IO) {
//                Jsoup.connect(discoverURL).timeout(10 * 1000).get()
//            }
//            DocumentParseUtil.parseBannerData(documentData)
//        }

//        val banner = SaveKeyValues.getValue("banner", "") as String
//        if (banner != "") {
//            val type = object : TypeToken<ArrayList<BannerBean>>() {}.type
//
//            val bannerBeanList: ArrayList<BannerBean> = Gson().fromJson(banner, type)
//            //轮播图
//            val bannerImageAdapter = BannerImageAdapter(context, bannerBeanList)
//            discoverBanner.addBannerLifecycleObserver(this)
//                .setAdapter(bannerImageAdapter)
//                .setIndicator(CircleIndicator(context))
//                .start()
//            bannerImageAdapter.setOnItemClickListener(object :
//                OnItemClickListener {
//                override fun onItemClickListener(position: Int) {
//                    //查看大图
//                    showBigPicture(bannerBeanList[position].bannerImage)
//                }
//            })
//        }


//        GlobalScope.launch(Dispatchers.Main) {
//            val discoverDocument = withContext(Dispatchers.IO) {
//                Jsoup.connect(discoverURL).timeout(10 * 1000).get()
//            }
//
//        }
    }
}