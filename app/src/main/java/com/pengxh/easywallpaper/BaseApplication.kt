package com.pengxh.easywallpaper

import android.app.Application
import com.pengxh.app.multilib.utils.SaveKeyValues
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.utils.Constant
import com.pengxh.easywallpaper.utils.SQLiteUtil
import com.tencent.bugly.crashreport.CrashReport
import kotlin.properties.Delegates

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 14:24
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        SaveKeyValues.initSharedPreferences(this)
        EasyToast.init(this)
        SQLiteUtil.initDataBase(this)
        CrashReport.initCrashReport(this, Constant.APP_ID, false)
    }

    /**
     * 委托模式单例化
     * */
    companion object {
        private var application: BaseApplication by Delegates.notNull()
        fun instance() = application
    }
}