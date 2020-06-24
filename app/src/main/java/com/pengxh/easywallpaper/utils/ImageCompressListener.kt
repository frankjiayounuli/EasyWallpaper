package com.pengxh.easywallpaper.utils

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/4/22 15:05
 */
interface ImageCompressListener {
    fun onSuccess(filePath: String)

    fun onFailure(throwable: Throwable)
}