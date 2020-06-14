package com.pengxh.easywallpaper.utils

import org.jsoup.nodes.Document

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/14 16:31
 */
interface IHttpListener {
    fun onSuccess(result: Document)

    fun onFailure(e: Exception)
}