package com.pengxh.easywallpaper.bean

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/16 11:25
 */
class DiscoverBean {
    lateinit var discoverTitle: String//标题

    lateinit var discoverSynopsis: String//简介

    lateinit var bigImage: String//大图

    lateinit var smallImages: ArrayList<SmallImageBean>//小图集合

    lateinit var discoverURL: String//探索发现详情地址

    class SmallImageBean {
        lateinit var smallImage: String//小图
    }
}