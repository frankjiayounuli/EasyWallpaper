package com.pengxh.easywallpaper.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.WallpaperAdapter
import com.pengxh.easywallpaper.bean.WallpaperBean
import com.pengxh.easywallpaper.utils.*
import kotlinx.android.synthetic.main.activity_star_personal.*
import kotlinx.android.synthetic.main.include_title.*
import org.jsoup.nodes.Document

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/21 1:02
 */
class StarPersonalActivity : BaseNormalActivity() {

    companion object {
        private const val Tag: String = "StarPersonalActivity"
    }

    private val context = this
    private lateinit var link: String
    private var listBeans: ArrayList<WallpaperBean> = ArrayList()
    private lateinit var wallpaperAdapter: WallpaperAdapter
    private var defaultPage = 1
    private var isLoadMore = false

    override fun initLayoutView(): Int = R.layout.activity_star_personal

    override fun initData() {
        StatusBarColorUtil.setColor(this, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = intent.getStringExtra("pageTitle")
        mTitleRightView.visibility = View.GONE

        link = intent.getStringExtra("starPersonalLink")!!
        //根据链接获取所有图片
        HttpHelper.getDocumentData(link, object : HttpListener {
            override fun onSuccess(result: Document) {
                listBeans = HTMLParseUtil.parseStarPersonalData(result)
                handler.sendEmptyMessage(5000)
            }

            override fun onFailure(e: Exception) {
                handler.sendEmptyMessage(5001)
            }
        })
    }

    override fun initEvent() {
        starRefreshLayout.setEnableRefresh(false)
        starRefreshLayout.setOnLoadMoreListener {
            Log.d(Tag, "onLoadMore: 上拉加载")
            isLoadMore = true
            defaultPage++
            HttpHelper.loadMoreStarWallpaper(defaultPage, link, object : HttpListener {
                override fun onSuccess(result: Document) {
                    //加载更多
                    listBeans.addAll(HTMLParseUtil.parseStarPersonalData(result))
                    handler.sendEmptyMessage(5000)
                }

                override fun onFailure(e: Exception) {
                    handler.sendEmptyMessage(5001)
                }
            })
        }
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                5000 -> {
                    if (isLoadMore) {
                        wallpaperAdapter.notifyDataSetChanged()
                    } else {
                        //首次加载数据
                        wallpaperAdapter = WallpaperAdapter(context, listBeans)
                        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        starRecyclerView.layoutManager = staggeredGridLayoutManager
                        starRecyclerView.adapter = wallpaperAdapter
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
                5001 -> {
                    EasyToast.showToast("已经到底了，别拉了~", EasyToast.DEFAULT)
                }
            }
            //不管成功与否，都结束加载
            starRefreshLayout.finishLoadMore()
        }
    }
}