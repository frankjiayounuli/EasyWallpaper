package com.pengxh.easywallpaper.utils

import com.pengxh.easywallpaper.R

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 13:54
 */
object Constant {
    //轮播图测试地址
    val bannerImages = listOf(
        "http://pic1.win4000.com/tj/2020-05-20/5ec486e98fb99.jpg",
        "http://pic1.win4000.com/tj/2020-06-08/5edd9c4d63d42.jpg",
        "http://pic1.win4000.com/tj/2019-09-21/5d85cb123c076.jpg",
        "http://pic1.win4000.com/tj/2020-04-02/5e85af6a9d261.jpg"
    )

    val itemImages = arrayOf(
        R.mipmap.category,
        R.mipmap.screen,
        R.mipmap.d_wallpaper,
        R.mipmap.special
    )
    val items = arrayOf("分类", "横屏", "动态壁纸", "专题")

    //根地址
    const val BaseURL = "http://www.win4000.com"
    //横屏壁纸地址
    const val BannerTargetURL = "$BaseURL/wallpaper.html"
    //手机壁纸根地址
    const val URL = "$BaseURL/mobile.html"
}