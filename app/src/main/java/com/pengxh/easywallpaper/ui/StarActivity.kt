package com.pengxh.easywallpaper.ui

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.CategoryAdapter
import com.pengxh.easywallpaper.adapter.CircleImageAdapter
import com.pengxh.easywallpaper.adapter.WallpaperAdapter
import com.pengxh.easywallpaper.utils.*
import kotlinx.android.synthetic.main.activity_category.childRecyclerView
import kotlinx.android.synthetic.main.activity_category.parentListView
import kotlinx.android.synthetic.main.activity_star.*
import kotlinx.android.synthetic.main.include_title.*
import org.jsoup.nodes.Document

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/19 11:55
 */
class StarActivity : BaseNormalActivity() {

    companion object {
        private const val Tag = "StarActivity"
    }

    private val context = this@StarActivity

    override fun initLayoutView(): Int = R.layout.activity_star

    override fun initData() {
        StatusBarColorUtil.setColor(this, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = "明星写真"
        mTitleRightView.visibility = View.GONE
    }

    override fun initEvent() {
        HttpHelper.getDocumentData(Constant.StarURL, object : HttpListener {
            override fun onSuccess(result: Document) {
                val categoryData = HTMLParseUtil.parseStarData(result)

                parentListView.adapter = CategoryAdapter(context, categoryData)
                //默认加载第一条数据显示
                HttpHelper.getDocumentData(categoryData[0].categoryLink, object : HttpListener {
                    override fun onSuccess(result: Document) {
                        bindTopChildData(result)
                    }

                    override fun onFailure(e: Exception) {

                    }
                })

                parentListView.setOnItemClickListener { parent, view, position, id ->
                    if (position == 0) {
                        bindTopChildData(result)
                    } else {
                        bindChildData(categoryData[position].categoryLink)
                    }
                }
            }

            override fun onFailure(e: Exception) {

            }
        })

        searchBar.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }
    }

    //绑定顶部圆形头像
    private fun bindTopChildData(document: Document) {
        val circleImageData = HTMLParseUtil.parseCircleImageData(document)
        val circleImageAdapter = CircleImageAdapter(context, circleImageData)
        childRecyclerView.layoutManager = GridLayoutManager(context, 2)
        childRecyclerView.adapter = circleImageAdapter
        circleImageAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                val bean = circleImageData[position]
                startStarPersonalActivity(bean.wallpaperURL, bean.wallpaperTitle)
            }
        })
    }

    private fun bindChildData(link: String) {
        HttpHelper.getDocumentData(link, object : HttpListener {
            override fun onSuccess(result: Document) {
                val wallpaperData = HTMLParseUtil.parseWallpaperUpdateData(result)
                val wallpaperAdapter = WallpaperAdapter(context, wallpaperData)
                val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                childRecyclerView.layoutManager = staggeredGridLayoutManager
                childRecyclerView.adapter = wallpaperAdapter

                wallpaperAdapter.setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClickListener(position: Int) {
                        val bean = wallpaperData[position]
                        startStarPersonalActivity(bean.wallpaperURL, bean.wallpaperTitle)
                    }
                })
            }

            override fun onFailure(e: Exception) {

            }
        })
    }

    private fun startStarPersonalActivity(link: String, title: String) {
        if (link == "") {
            EasyToast.showToast("加载失败，请稍后重试", EasyToast.WARING)
        } else {
            val intent = Intent(context, StarPersonalActivity::class.java)
            intent.putExtra("pageTitle", title)
            intent.putExtra("starPersonalLink", link)
            startActivity(intent)
        }
    }
}