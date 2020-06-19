package com.pengxh.easywallpaper.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.ParentAdapter
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

    private var context: Context = this@CategoryActivity

    override fun initLayoutView(): Int {
        return R.layout.activity_category
    }

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

                parentListView.adapter = ParentAdapter(context, categoryData)
                parentListView.setItemChecked(0, true)
                parentListView.setOnItemClickListener { parent, view, position, id ->
                    //点击的时候绑定第二个布局数据
                    HttpHelper.getDocumentData(
                        categoryData[position].categoryLink,
                        object : HttpListener {
                            override fun onSuccess(result: Document) {

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
}