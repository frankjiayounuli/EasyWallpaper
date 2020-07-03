package com.pengxh.easywallpaper.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.HeadImageAdapter
import com.pengxh.easywallpaper.bean.HeadImageBean
import com.pengxh.easywallpaper.utils.HTMLParseUtil
import com.pengxh.easywallpaper.utils.HttpHelper
import com.pengxh.easywallpaper.utils.HttpListener
import com.pengxh.easywallpaper.utils.StatusBarColorUtil
import kotlinx.android.synthetic.main.activity_head.*
import kotlinx.android.synthetic.main.include_title.*
import me.shaohui.bottomdialog.BottomDialog
import org.jsoup.nodes.Document

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/7/3 14:41
 */
class HeadImageActivity : BaseNormalActivity() {

    companion object {
        private const val Tag = "HeadImageActivity"
    }

    private val context: Context = this@HeadImageActivity
    private var defaultPage = 1
    private var isLoadMore = false
    private var dataList = ArrayList<HeadImageBean>()
    private lateinit var headImageAdapter: HeadImageAdapter

    override fun initLayoutView(): Int = R.layout.activity_head

    override fun initData() {
        StatusBarColorUtil.setColor(this, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = "精选头像"
        mTitleRightView.visibility = View.GONE

        //加载默认数据页
        HttpHelper.getHeadImageData(defaultPage, object : HttpListener {
            override fun onSuccess(result: Document) {
                dataList = HTMLParseUtil.parseHeadImageData(result)
                handler.sendEmptyMessage(6000)
            }

            override fun onFailure(e: Exception) {
                handler.sendEmptyMessage(6001)
            }
        })
    }

    override fun initEvent() {
        //下拉刷新
        headImageLayout.setEnableRefresh(false)
        //上拉加载
        headImageLayout.setOnLoadMoreListener {
            Log.d(Tag, "onLoadMore: 上拉加载")
            isLoadMore = true
            defaultPage++
            HttpHelper.getHeadImageData(defaultPage, object : HttpListener {
                override fun onSuccess(result: Document) {
                    //加载更多
                    dataList.addAll(HTMLParseUtil.parseHeadImageData(result))
                    handler.sendEmptyMessage(6000)
                }

                override fun onFailure(e: Exception) {
                    if (e.message == "IndexOutOfBoundsException") {
                        handler.sendEmptyMessage(6002)
                    } else {
                        handler.sendEmptyMessage(6001)
                    }
                }
            })
        }
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                6000 -> {
                    if (isLoadMore) {
                        headImageAdapter.notifyDataSetChanged()
                    } else {
                        Log.d(Tag, "首次加载数据")
                        headImageAdapter = HeadImageAdapter(context, dataList)
                        headImageGridView.adapter = headImageAdapter
                    }
                    headImageGridView.setOnItemClickListener { parent, view, position, id ->
                        val headImageLink = dataList[position].headImageLink
                        //TODO BottomSheet展示高清图像
                        BottomDialog.create(supportFragmentManager)
                            .setLayoutRes(R.layout.bottom_sheet)
                            .setViewListener {
                                //TODO 会空指针，待Fix
                                val headImageView = it.findViewById<ImageView>(R.id.headImageView)
                                headImageView.setBackgroundResource(R.drawable.test)
                            }
                            .setDimAmount(0.1f)
                            .setCancelOutside(false)
                            .show();
                    }
                }
                6001 -> {
                    EasyToast.showToast("加载失败，请稍后重试", EasyToast.ERROR)
                }
                6002 -> {
                    EasyToast.showToast("已经到底了，别拉了~", EasyToast.DEFAULT)
                }
            }
            headImageLayout.finishLoadMore()
        }
    }
}