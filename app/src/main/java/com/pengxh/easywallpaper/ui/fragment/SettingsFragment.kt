package com.pengxh.easywallpaper.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.BaseFragment
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.ui.AboutActivity
import com.pengxh.easywallpaper.utils.FileUtil
import com.pengxh.easywallpaper.utils.StatusBarColorUtil
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.include_title.*
import java.io.File

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 15:44
 */
class SettingsFragment : BaseFragment() {

    companion object {
        private const val Tag = "SettingsFragment"
    }

    override fun initLayoutView(): Int = R.layout.fragment_settings

    override fun initData() {
        StatusBarColorUtil.setColor(activity, Color.WHITE)
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init()

        mTitleLeftView.visibility = View.GONE
        mTitleView.text = "设置中心"
        mTitleRightView.visibility = View.GONE
    }

    override fun initEvent() {
        /**
         * /data/data/com.pengxh.easywallpaper/cache
         * */
        val file = File(context!!.cacheDir.absolutePath)
        val fileSize = FileUtil.getFileSize(file)
        cacheSize.text = FileUtil.formatFileSize(fileSize)
        settingLayout.setOnClickListener {
            FileUtil.deleteFile(file)
            cacheSize.text = FileUtil.formatFileSize(0)
        }

        aboutLayout.setOnClickListener {
            startActivity(Intent(activity, AboutActivity::class.java))
        }

        issueLayout.setOnClickListener {
            EasyToast.showToast("暂未开发", EasyToast.WARING)
        }
    }
}