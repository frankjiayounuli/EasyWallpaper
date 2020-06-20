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
import com.pengxh.easywallpaper.adapter.WallpaperAdapter
import com.pengxh.easywallpaper.bean.WallpaperBean
import com.pengxh.easywallpaper.ui.CategoryActivity
import com.pengxh.easywallpaper.ui.SpecialActivity
import com.pengxh.easywallpaper.ui.StarActivity
import com.pengxh.easywallpaper.ui.WallpaperActivity
import com.pengxh.easywallpaper.utils.*
import kotlinx.android.synthetic.main.fragment_home.*
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
        private const val Tag = "HomePageFragment"
    }

    private var defaultPage = 1
    private var listBeans: ArrayList<WallpaperBean> = ArrayList()
    private var isLoadMore = false
    private lateinit var wallpaperAdapter: WallpaperAdapter

    override fun initLayoutView(): Int = R.layout.fragment_home

    override fun initData() {
        StatusBarColorUtil.setColor(activity, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = "壁纸推荐"
        mTitleRightView.visibility = View.GONE
    }

    override fun initEvent() {
        //四个选项按钮
        initButtonEvent()

        //最新手机壁纸
        val wallpaperData = SaveKeyValues.getValue("wallpaperData", "") as String
        if (wallpaperData != "") {
            val type = object : TypeToken<ArrayList<WallpaperBean>>() {}.type
            listBeans = Gson().fromJson(wallpaperData, type)
            handler.sendEmptyMessage(1000)
        } else {
            handler.sendEmptyMessage(1001)
        }

        //上拉加载
        wallpaperLayout.setOnLoadMoreListener {
            Log.d(Tag, "onLoadMore: 上拉加载")
            isLoadMore = true
            defaultPage++
            HttpHelper.getWallpaperUpdate(defaultPage, object : HttpListener {
                override fun onSuccess(result: Document) {
                    //加载更多
                    listBeans.addAll(HTMLParseUtil.parseWallpaperUpdateData(result))
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
                        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                            2,
                            StaggeredGridLayoutManager.VERTICAL
                        )
                        wallpaperRecyclerView.layoutManager = staggeredGridLayoutManager
                        wallpaperRecyclerView.adapter = wallpaperAdapter
                    }
                    wallpaperAdapter.setOnItemClickListener(object : OnItemClickListener {
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

    private fun initButtonEvent() {
        categoryButton.setOnClickListener {
            startActivity(Intent(context, CategoryActivity::class.java))
        }
        starButton.setOnClickListener {
            startActivity(Intent(context, StarActivity::class.java))
        }
        specialButton.setOnClickListener {
            startActivity(Intent(context, SpecialActivity::class.java))
        }
    }
}