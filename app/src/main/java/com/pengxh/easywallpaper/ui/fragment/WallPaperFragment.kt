package com.pengxh.easywallpaper.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.utils.SaveKeyValues
import com.pengxh.easywallpaper.BaseFragment
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.BannerImageAdapter
import com.pengxh.easywallpaper.adapter.HorizontalAdapter
import com.pengxh.easywallpaper.bean.BannerBean
import com.pengxh.easywallpaper.ui.BigPictureActivity
import com.pengxh.easywallpaper.utils.RecyclerItemDecoration
import com.pengxh.easywallpaper.utils.StatusBarColorUtil
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_wallpaper.*
import kotlinx.android.synthetic.main.include_title.*

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 15:58
 */
class WallPaperFragment : BaseFragment() {

    companion object {
        private const val Tag = "WallPaperFragment"
    }

    override fun initLayoutView(): Int {
        return R.layout.fragment_wallpaper
    }

    override fun initData() {
        StatusBarColorUtil.setColor(activity, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = "高清壁纸"
        mTitleRightView.visibility = View.GONE
    }

    override fun initEvent() {
        //获取爬虫抓取的Banner数据
        val banner = SaveKeyValues.getValue("banner", "") as String
        if (banner != "") {
            val type = object : TypeToken<ArrayList<BannerBean>>() {}.type

            val bannerBeanList: ArrayList<BannerBean> = Gson().fromJson(banner, type)
            //轮播图
            val bannerImageAdapter = context?.let { BannerImageAdapter(it, bannerBeanList) }
            wallpaperBanner.addBannerLifecycleObserver(this)
                .setAdapter(bannerImageAdapter)
                .setIndicator(CircleIndicator(context))
                .start()
            bannerImageAdapter!!.setOnItemClickListener(object :
                BannerImageAdapter.OnItemClickListener {
                override fun onItemClickListener(position: Int) {
                    //查看大图
                    val intent = Intent(context, BigPictureActivity::class.java)
                    intent.putExtra("imageURL", bannerBeanList[position].bannerImage)
                    startActivity(intent)
                }
            })
        }

        //四个选项按钮
        val horizontalAdapter = context?.let { HorizontalAdapter(it) }
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL // 横向滚动
        horizontalRecyclerView.layoutManager = layoutManager
        horizontalRecyclerView.addItemDecoration(RecyclerItemDecoration(4))
        horizontalRecyclerView.adapter = horizontalAdapter
        horizontalAdapter!!.setOnItemClickListener(object : HorizontalAdapter.OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                Log.d(Tag, ": $position")
            }
        })
    }
}