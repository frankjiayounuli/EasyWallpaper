package com.pengxh.easywallpaper.ui

import android.graphics.Color
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.StatusBarColorUtil
import kotlinx.android.synthetic.main.include_title.*

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/22 17:47
 */
class IssueActivity : BaseNormalActivity() {

    override fun initLayoutView(): Int = R.layout.activity_issue

    override fun initData() {
        StatusBarColorUtil.setColor(this, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleView.text = "问题反馈"
        mTitleRightView.visibility = View.GONE
    }

    override fun initEvent() {
        mTitleLeftView.setOnClickListener {
            this.finish()
        }
    }
}