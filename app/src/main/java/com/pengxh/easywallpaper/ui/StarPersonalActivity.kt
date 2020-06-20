package com.pengxh.easywallpaper.ui

import android.graphics.Color
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.StatusBarColorUtil
import kotlinx.android.synthetic.main.include_title.*

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

    override fun initLayoutView(): Int = R.layout.activity_star_personal

    override fun initData() {
        StatusBarColorUtil.setColor(this, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = intent.getStringExtra("pageTitle")
        mTitleRightView.visibility = View.GONE

        val link = intent.getStringExtra("starPersonalLink")
        //根据链接获取所有图片
    }

    override fun initEvent() {

    }
}