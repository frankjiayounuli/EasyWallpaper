package com.pengxh.easywallpaper.utils

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 13:54
 */
object Constant {
    //根地址
    private const val BaseURL = "http://www.win4000.com"

    //最新手机壁纸地址，最多5页
    var WallpaperUpdateURL = "$BaseURL/mobile_0_0_0_index.html"

    //发现
    var DiscoverURL = "$BaseURL/hj/index.html"

    //壁纸分类，采用二级联动菜单
    const val CategoryURL = "$BaseURL/mobile_0_0_0_1.html"

    //明星写真
    const val s = "$BaseURL/mt/star.html"

    //壁纸专题
    const val z = "$BaseURL/zt/mobile.html"
}