package com.pengxh.easywallpaper.ui

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aihook.alertview.library.AlertView
import com.aihook.alertview.library.OnItemClickListener
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.HistoryAdapter
import com.pengxh.easywallpaper.adapter.WallpaperAdapter
import com.pengxh.easywallpaper.bean.StarNameBean
import com.pengxh.easywallpaper.utils.*
import kotlinx.android.synthetic.main.activity_search.*
import org.jsoup.nodes.Document

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/20 23:31
 */
class SearchActivity : BaseNormalActivity() {

    companion object {
        private const val Tag: String = "SearchActivity"
    }

    private val context = this@SearchActivity
    private val sqLiteUtil = SQLiteUtil.getInstance()

    override fun initLayoutView(): Int = R.layout.activity_search

    override fun initData() {
        StatusBarColorUtil.setColor(this, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()
    }

    override fun initEvent() {
        mTitleRightView.setOnClickListener {
            val starName = searchView.text.toString().trim()
            if (starName == "") {
                EasyToast.showToast("哎呀，什么都还没输入呢~", EasyToast.WARING)
                return@setOnClickListener
            }
            //把搜索历史存入数据库
            sqLiteUtil.saveStar(starName)

            searchData(starName)
        }

        mTitleLeftView.setOnClickListener {
            this.finish()
        }

        val allHistory = sqLiteUtil.loadAllHistory()
        if (allHistory.size == 0) {
            emptyLayout.visibility = View.VISIBLE
            historyLayout.visibility = View.GONE
        } else {
            historyLayout.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
            //绑定adapter
            historyListView.adapter = HistoryAdapter(context, allHistory as ArrayList<StarNameBean>)
            historyListView.setOnItemClickListener { parent, view, position, id ->
                val name = allHistory[position].starName

                searchView.setText(name)
                searchData(name)
            }

            //删除搜索记录
            deleteHistory.setOnClickListener {
                AlertView("提示", "是否清空搜索记录", "取消", arrayOf("确定"), null, context, AlertView.Style.Alert,
                    OnItemClickListener { o, position ->
                        if (position == 0) {
                            sqLiteUtil.deleteAll()
                            historyLayout.visibility = View.GONE
                            emptyLayout.visibility = View.VISIBLE
                        }
                    }).setCancelable(false).show()
            }
        }
    }

    private fun searchData(name: String) {
        val searchURL = Constant.SearchAction.replace("keyword", name)
        HttpHelper.getDocumentData(searchURL, object : HttpListener {
            override fun onSuccess(result: Document) {
                emptyLayout.visibility = View.GONE
                searchResult.visibility = View.VISIBLE
                //可能会搜不到结果，但是result一定不会空
                val searchData = HTMLParseUtil.parseSearchData(result)
                //展示搜索到的数据
                val wallpaperAdapter = WallpaperAdapter(context, searchData)
                val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL
                )
                searchResult.layoutManager = staggeredGridLayoutManager
                searchResult.adapter = wallpaperAdapter

                wallpaperAdapter.setOnItemClickListener(object : com.pengxh.easywallpaper.utils.OnItemClickListener {
                    override fun onItemClickListener(position: Int) {
                        //跳转相应的壁纸分类
                        val wallpaperBean = searchData[position]
                        val link = wallpaperBean.wallpaperURL
                        if (link == "") {
                            EasyToast.showToast("加载失败，请稍后重试", EasyToast.WARING)
                        } else {
                            val intent = Intent(context, StarPersonalActivity::class.java)
                            intent.putExtra("pageTitle", wallpaperBean.wallpaperTitle)
                            intent.putExtra("starPersonalLink", link)
                            startActivity(intent)
                        }
                    }
                })
            }

            override fun onFailure(e: Exception) {
                emptyLayout.visibility = View.VISIBLE
                searchResult.visibility = View.GONE
                EasyToast.showToast("抱歉！暂无数据", EasyToast.ERROR)
            }
        })
    }
}