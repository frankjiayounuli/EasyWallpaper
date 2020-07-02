package com.pengxh.easywallpaper.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.BaseFragment
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.DiscoverAdapter
import com.pengxh.easywallpaper.bean.DiscoverBean
import com.pengxh.easywallpaper.ui.DiscoverDetailActivity
import com.pengxh.easywallpaper.utils.*
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.include_title.*
import org.jsoup.nodes.Document

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 15:44
 */
class DiscoverFragment : BaseFragment() {

    companion object {
        private const val Tag = "DiscoverFragment"
    }

    private var defaultPage = 1
    private var isLoadMore = false
    private var discoverList = ArrayList<DiscoverBean>()
    private lateinit var discoverAdapter: DiscoverAdapter

    override fun initLayoutView(): Int = R.layout.fragment_discover


    override fun initData() {
        StatusBarColorUtil.setColor(activity, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = "探索发现"
        mTitleRightView.visibility = View.GONE

        HttpHelper.getDiscoverData(defaultPage, object : HttpListener {
            override fun onSuccess(result: Document) {
                //默认加载第一页数据
                discoverList = HTMLParseUtil.parseDiscoverData(result)
                handler.sendEmptyMessage(2000)
            }

            override fun onFailure(e: Exception) {
                handler.sendEmptyMessage(2001)
            }
        })
    }

    override fun initEvent() {
        //下拉刷新
        discoverLayout.setEnableRefresh(false)
        //上拉加载
        discoverLayout.setOnLoadMoreListener {
            Log.d(Tag, "onLoadMore: 上拉加载")
            isLoadMore = true
            defaultPage++
            HttpHelper.getDiscoverData(defaultPage, object : HttpListener {
                override fun onSuccess(result: Document) {
                    //加载更多
                    discoverList.addAll(HTMLParseUtil.parseDiscoverData(result))
                    handler.sendEmptyMessage(2000)
                }

                override fun onFailure(e: Exception) {
                    if (e.message == "IndexOutOfBoundsException") {
                        handler.sendEmptyMessage(2002)
                    } else {
                        handler.sendEmptyMessage(2001)
                    }
                }
            })
        }
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                2000 -> {
                    if (isLoadMore) {
                        discoverAdapter.notifyDataSetChanged()
                    } else {
                        Log.d(Tag, "首次加载数据")
                        discoverAdapter = DiscoverAdapter(context!!, discoverList)
                        discoverRecyclerView.layoutManager = LinearLayoutManager(context)
                        discoverRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                        discoverRecyclerView.adapter = discoverAdapter
                    }
                    discoverAdapter.setOnItemClickListener(object : OnItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            val intent = Intent(context, DiscoverDetailActivity::class.java)
                            intent.putExtra("discoverTitle", discoverList[position].discoverTitle)
                            intent.putExtra("discoverURL", discoverList[position].discoverURL)
                            startActivity(intent)
                        }
                    })
                }
                2001 -> {
                    EasyToast.showToast("加载失败，请稍后重试", EasyToast.ERROR)
                }
                2002 -> {
                    EasyToast.showToast("已经到底了，别拉了~", EasyToast.DEFAULT)
                }
            }
            discoverLayout.finishLoadMore()
        }
    }
}