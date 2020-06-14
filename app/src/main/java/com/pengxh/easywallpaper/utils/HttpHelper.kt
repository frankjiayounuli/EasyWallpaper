package com.pengxh.easywallpaper.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/14 16:26
 */
class HttpHelper {
    companion object {
        private const val Tag: String = "HttpHelper"

        /**
         * 获取最新壁纸
         * */
        fun getWallpaperUpdate(pageNumber: Int, listener: IHttpListener) {
            if (pageNumber >= 6) {
                listener.onFailure(IndexOutOfBoundsException("IndexOutOfBoundsException"))
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    val document = withContext(Dispatchers.IO) {
                        Jsoup.connect(
                            Constant.WallpaperUpdateURL.replace(
                                "index",
                                pageNumber.toString(),
                                true
                            )
                        ).timeout(10 * 1000).get()
                    }
                    if (document == null) {
                        listener.onFailure(NullPointerException())
                    } else {
                        listener.onSuccess(document)
                    }
                }
            }
        }
    }
}