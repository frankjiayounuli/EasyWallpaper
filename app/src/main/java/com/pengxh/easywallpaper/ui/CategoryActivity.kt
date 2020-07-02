package com.pengxh.easywallpaper.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.CategoryAdapter
import com.pengxh.easywallpaper.adapter.WallpaperAdapter
import com.pengxh.easywallpaper.bean.WallpaperBean
import com.pengxh.easywallpaper.utils.*
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.include_title.*
import org.jsoup.nodes.Document

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/19 11:27
 */
class CategoryActivity : BaseNormalActivity() {

    companion object {
        private const val Tag = "CategoryActivity"
    }

    private var context = this@CategoryActivity
    private var defaultPage = 1
    private var isLoadMore = false
    private lateinit var link: String//二级联动第一页数据的地址
    private var wallpaperData: ArrayList<WallpaperBean> = ArrayList()
    private lateinit var wallpaperAdapter: WallpaperAdapter

    override fun initLayoutView(): Int = R.layout.activity_category

    override fun initData() {
        StatusBarColorUtil.setColor(this, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = "壁纸分类"
        mTitleRightView.visibility = View.GONE
    }

    override fun initEvent() {
        HttpHelper.getDocumentData(Constant.CategoryURL, object : HttpListener {
            override fun onSuccess(result: Document) {
                val categoryData = HTMLParseUtil.parseCategoryData(result)
                parentListView.adapter = CategoryAdapter(context, categoryData)
                //默认加载第一条数据显示
                link = categoryData[0].categoryLink
                HttpHelper.getDocumentData(link, object : HttpListener {
                    override fun onSuccess(result: Document) {
                        val obtainMessage = handler.obtainMessage()
                        obtainMessage.what = 3000
                        obtainMessage.obj = result
                        handler.sendMessage(obtainMessage)
                    }

                    override fun onFailure(e: Exception) {

                    }
                })

                //点击实现二级联动
                parentListView.setOnItemClickListener { parent, view, position, id ->
                    //点击的时候绑定第二个布局数据，得到第一页的链接地址并且重置页码
                    defaultPage = 1
                    link = categoryData[position].categoryLink
                    HttpHelper.getDocumentData(link, object : HttpListener {
                        override fun onSuccess(result: Document) {
                            bindChildData(result)
                        }

                        override fun onFailure(e: Exception) {

                        }
                    })
                }
            }

            override fun onFailure(e: Exception) {

            }
        })

        categoryRefreshLayout.setEnableRefresh(false)
        //上拉加载
        categoryRefreshLayout.setOnLoadMoreListener {
            isLoadMore = true
            defaultPage++
            HttpHelper.loadMoreWallpaper(defaultPage, link, object : HttpListener {
                override fun onSuccess(result: Document) {
                    //加载更多
                    wallpaperData.addAll(HTMLParseUtil.parseWallpaperUpdateData(result))
                    handler.sendEmptyMessage(3000)
                }

                override fun onFailure(e: Exception) {
                    if (e.message == "IndexOutOfBoundsException") {
                        handler.sendEmptyMessage(3002)
                    } else {
                        handler.sendEmptyMessage(3001)
                    }
                }
            })
        }
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                3000 -> {
                    if (isLoadMore) {
                        wallpaperAdapter.notifyDataSetChanged()
                    } else {
                        //首次加载数据
                        val document = msg.obj as Document
                        wallpaperData = HTMLParseUtil.parseWallpaperUpdateData(document)
                        wallpaperAdapter = WallpaperAdapter(context, wallpaperData)
                        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        childRecyclerView.layoutManager = staggeredGridLayoutManager
                        childRecyclerView.adapter = wallpaperAdapter
                    }
                    wallpaperAdapter.setOnItemClickListener(object : OnItemClickListener {
                        override fun onItemClickListener(position: Int) {
                            //跳转相应的壁纸分类
                            val wallpaperURL = wallpaperData[position].wallpaperURL
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
                3001 -> {
                    EasyToast.showToast("加载失败，请稍后重试", EasyToast.ERROR)
                }
                3002 -> {
                    EasyToast.showToast("已经到底了，别拉了~", EasyToast.DEFAULT)
                }
            }
            //不管成功与否，都结束加载
            categoryRefreshLayout.finishLoadMore()
        }
    }

    private fun bindChildData(document: Document) {
        wallpaperData = HTMLParseUtil.parseWallpaperUpdateData(document)
        val adapter = WallpaperAdapter(context, wallpaperData)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        childRecyclerView.layoutManager = staggeredGridLayoutManager
        childRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                //跳转相应的壁纸分类
                val wallpaperURL = wallpaperData[position].wallpaperURL
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
}