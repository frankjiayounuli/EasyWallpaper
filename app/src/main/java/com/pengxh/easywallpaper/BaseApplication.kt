package com.pengxh.easywallpaper

import android.app.Application
import com.pengxh.app.multilib.utils.SaveKeyValues
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.utils.SQLiteUtil

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 14:24
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SaveKeyValues.initSharedPreferences(this)
        EasyToast.init(this)
        SQLiteUtil.initDataBase(this)
    }
}