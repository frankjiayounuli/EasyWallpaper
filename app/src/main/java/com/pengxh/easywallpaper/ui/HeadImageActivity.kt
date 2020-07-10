package com.pengxh.easywallpaper.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.gyf.immersionbar.ImmersionBar
import com.makeramen.roundedimageview.RoundedImageView
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.widget.CircleImageView
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.HeadImageAdapter
import com.pengxh.easywallpaper.bean.HeadImageBean
import com.pengxh.easywallpaper.utils.*
import kotlinx.android.synthetic.main.activity_head.*
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        startHttpRequest(defaultPage)
    }

    override fun initEvent() {
        //下拉刷新
        headImageLayout.setEnableRefresh(false)
        //上拉加载
        headImageLayout.setOnLoadMoreListener {
            Log.d(Tag, "onLoadMore: 上拉加载")
            isLoadMore = true
            defaultPage++
            startHttpRequest(defaultPage)
        }
    }

    private fun startHttpRequest(page: Int) {
        HttpHelper.getHeadImageData(page, object : HttpListener {
            override fun onSuccess(result: Document) {
                //加载更多
                dataList.addAll(HTMLParseUtil.parseHeadImageData(result))
                handler.sendEmptyMessage(6000)
            }

            override fun onFailure(e: Exception) {
                handler.sendEmptyMessage(6001)
            }
        })
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

                        BottomDialog.create(supportFragmentManager)
                            .setLayoutRes(R.layout.bottom_sheet)
                            .setViewListener {
                                val saveHeadImage = it.findViewById(R.id.saveHeadImage) as Button
                                val originalImageView = it.findViewById(R.id.originalImageView) as ImageView
                                val weChatImageView = it.findViewById(R.id.weChatImageView) as RoundedImageView
                                val qqImageView = it.findViewById(R.id.qqImageView) as CircleImageView
                                val radioGroup = it.findViewById(R.id.radioGroup) as RadioGroup

                                HttpHelper.getDocumentData(headImageLink, object : HttpListener {
                                    override fun onSuccess(result: Document) {
                                        val imageURL = HTMLParseUtil.parseHeadImageURL(result)
                                        val imageBuilder = Glide.with(context)
                                            .load(imageURL)
                                            .apply(RequestOptions().placeholder(R.drawable.ic_empty))

                                        //默认显示原图
                                        imageBuilder.into(originalImageView)
                                        radioGroup.setOnCheckedChangeListener { group, checkedId ->
                                            when (checkedId) {
                                                R.id.originalButton -> {
                                                    weChatImageView.visibility = View.GONE
                                                    qqImageView.visibility = View.GONE
                                                    originalImageView.visibility = View.VISIBLE

                                                    imageBuilder.into(originalImageView)
                                                }
                                                R.id.weChatButton -> {
                                                    originalImageView.visibility = View.GONE
                                                    qqImageView.visibility = View.GONE
                                                    weChatImageView.visibility = View.VISIBLE

                                                    imageBuilder.into(weChatImageView)
                                                }
                                                R.id.qqButton -> {
                                                    originalImageView.visibility = View.GONE
                                                    weChatImageView.visibility = View.GONE
                                                    qqImageView.visibility = View.VISIBLE

                                                    imageBuilder.into(qqImageView)
                                                }
                                            }
                                        }

                                        //保存高清头像
                                        saveHeadImage.setOnClickListener {
                                            GlobalScope.launch(Dispatchers.Main) {
                                                val drawable = withContext(Dispatchers.IO) {
                                                    Glide.with(context).load(imageURL)
                                                        .apply(RequestOptions().centerCrop())
                                                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                                        .get() as BitmapDrawable
                                                }
                                                if (FileUtil.saveWallpaper(context, drawable)) {
                                                    EasyToast.showToast("保存成功", EasyToast.SUCCESS)
                                                } else {
                                                    EasyToast.showToast("保存失败", EasyToast.ERROR)
                                                }
                                            }
                                        }
                                    }

                                    override fun onFailure(e: Exception) {
                                        e.printStackTrace()
                                    }
                                })
                            }
                            .setDimAmount(0.5f)
                            .setCancelOutside(true)
                            .show()
                    }
                }
                6001 -> {
                    EasyToast.showToast("已经到底了，别拉了~", EasyToast.DEFAULT)
                }
            }
            headImageLayout.finishLoadMore()
        }
    }
}