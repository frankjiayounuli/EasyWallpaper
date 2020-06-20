package com.pengxh.easywallpaper.ui

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.CategoryAdapter
import com.pengxh.easywallpaper.adapter.WallpaperAdapter
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
                HttpHelper.getDocumentData(categoryData[0].categoryLink, object : HttpListener {
                    override fun onSuccess(result: Document) {
                        bindChildData(result)
                    }

                    override fun onFailure(e: Exception) {

                    }
                })

                //点击实现二级联动
                parentListView.setOnItemClickListener { parent, view, position, id ->
                    //点击的时候绑定第二个布局数据
                    HttpHelper.getDocumentData(
                        categoryData[position].categoryLink, object : HttpListener {
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
    }

    private fun bindChildData(document: Document) {
        val wallpaperData = HTMLParseUtil.parseWallpaperUpdateData(document)
        val wallpaperAdapter = WallpaperAdapter(context, wallpaperData)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL
        )
        childRecyclerView.layoutManager = staggeredGridLayoutManager
        childRecyclerView.adapter = wallpaperAdapter
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
}