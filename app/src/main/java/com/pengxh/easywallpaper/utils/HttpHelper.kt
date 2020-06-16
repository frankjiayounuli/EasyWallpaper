package com.pengxh.easywallpaper.utils

import android.util.Log
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
        fun getWallpaperUpdate(pageNumber: Int, listener: HttpListener) {
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

        /**
         * 获取探索发现数据
         * */
        fun getDiscoverData(pageNumber: Int, listener: HttpListener) {
            if (pageNumber >= 6) {
                listener.onFailure(IndexOutOfBoundsException("IndexOutOfBoundsException"))
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    val document = withContext(Dispatchers.IO) {
                        val url: String = if (pageNumber == 1) {
                            Constant.DiscoverURL
                        } else {
                            Constant.DiscoverURL.replace(
                                "index",
                                "index$pageNumber",
                                true
                            )
                        }
                        Log.d(Tag, "探索发现地址: $url")
                        Jsoup.connect(url).timeout(10 * 1000).get()
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