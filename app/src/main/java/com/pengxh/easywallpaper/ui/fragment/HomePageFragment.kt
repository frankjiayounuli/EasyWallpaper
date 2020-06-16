package com.pengxh.easywallpaper.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.utils.SaveKeyValues
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.BaseFragment
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.BannerImageAdapter
import com.pengxh.easywallpaper.adapter.WallpaperAdapter
import com.pengxh.easywallpaper.bean.BannerBean
import com.pengxh.easywallpaper.bean.WallpaperBean
import com.pengxh.easywallpaper.ui.BigPictureActivity
import com.pengxh.easywallpaper.ui.WallpaperActivity
import com.pengxh.easywallpaper.utils.*
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.include_category.*
import kotlinx.android.synthetic.main.include_title.*
import org.jsoup.nodes.Document

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 15:58
 */
class HomePageFragment : BaseFragment() {

    companion object {
        private const val Tag = "WallPaperFragment"
    }

    private var defaultPage = 1
    private var listBeans: ArrayList<WallpaperBean> = ArrayList()
    private var isLoadMore = false
    private lateinit var wallpaperAdapter: WallpaperAdapter

    override fun initLayoutView(): Int {
        return R.layout.fragment_home
    }

    override fun initData() {
        StatusBarColorUtil.setColor(activity, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = "壁纸推荐"
        mTitleRightView.visibility = View.GONE

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
                OnItemClickListener {
                override fun onItemClickListener(position: Int) {
                    //查看大图
                    showBigPicture(bannerBeanList[position].bannerImage)
                }
            })
        }
    }

    override fun initEvent() {
        //四个选项按钮
        initButtonEvent()

        //最新手机壁纸
        HttpHelper.getWallpaperUpdate(defaultPage, object : HttpListener {
            override fun onSuccess(result: Document) {
                //默认加载第一页数据
                listBeans = DocumentParseUtil.parseWallpaperUpdateData(result)
                handler.sendEmptyMessage(1000)
            }

            override fun onFailure(e: Exception) {
                handler.sendEmptyMessage(1001)
            }
        })

        //下拉刷新
        wallpaperLayout.setEnableRefresh(false)
        //上拉加载
        wallpaperLayout.setOnLoadMoreListener {
            Log.d(Tag, "onLoadMore: 上拉加载")
            isLoadMore = true
            defaultPage++
            HttpHelper.getWallpaperUpdate(defaultPage, object : HttpListener {
                override fun onSuccess(result: Document) {
                    //加载更多
                    listBeans.addAll(DocumentParseUtil.parseWallpaperUpdateData(result))
                    handler.sendEmptyMessage(1000)
                }

                override fun onFailure(e: Exception) {
                    if (e.message == "IndexOutOfBoundsException") {
                        handler.sendEmptyMessage(1002)
                    } else {
                        handler.sendEmptyMessage(1001)
                    }
                }
            })
        }
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1000 -> {
                    if (isLoadMore) {
                        wallpaperAdapter.notifyDataSetChanged()
                    } else {
                        //首次加载数据
                        wallpaperAdapter = WallpaperAdapter(context!!, listBeans)
                        wallpaperRecyclerView.layoutManager = StaggeredGridLayoutManager(
                            2,
                            StaggeredGridLayoutManager.VERTICAL
                        )
                        wallpaperRecyclerView.adapter = wallpaperAdapter
                    }
                    wallpaperAdapter.setOnItemClickListener(object :
                        OnItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            //跳转相应的壁纸分类
                            val wallpaperURL = listBeans[position].wallpaperURL
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
                1001 -> {
                    EasyToast.showToast("加载失败，请稍后重试", EasyToast.ERROR)
                }
                1002 -> {
                    EasyToast.showToast("已经到底了，别拉了~", EasyToast.DEFAULT)
                }
            }
            //不管成功与否，都结束加载
            wallpaperLayout.finishLoadMore()
        }
    }

    private fun showBigPicture(url: String) {
        //查看大图
        val intent = Intent(context, BigPictureActivity::class.java)
        intent.putExtra("imageURL", url)
        startActivity(intent)
    }

    private fun initButtonEvent() {
        //壁纸分类
        categoryLayout.setOnClickListener {
            EasyToast.showToast("壁纸分类", EasyToast.DEFAULT)
        }
        //横屏壁纸
        screenLayout.setOnClickListener {
            EasyToast.showToast("横屏壁纸", EasyToast.DEFAULT)
        }
        //动态壁纸
        dWallpaperLayout.setOnClickListener {
            EasyToast.showToast("动态壁纸", EasyToast.DEFAULT)
        }
        //壁纸专题
        specialLayout.setOnClickListener {
            EasyToast.showToast("壁纸专题", EasyToast.DEFAULT)
        }
    }
}