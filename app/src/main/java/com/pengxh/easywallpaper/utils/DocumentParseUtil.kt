package com.pengxh.easywallpaper.utils

import android.util.Log
import com.pengxh.easywallpaper.bean.BannerBean
import com.pengxh.easywallpaper.bean.DiscoverBean
import com.pengxh.easywallpaper.bean.WallpaperBean
import org.jsoup.nodes.Document

/**
 * @description: TODO document数据解析
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/12 23:42
 */
class DocumentParseUtil {
    companion object {
        private const val Tag: String = "DocumentParseUtil"

        /**
         * 解析Banner数据
         * */
        fun parseBannerData(document: Document): ArrayList<BannerBean> {
            val bannerList: ArrayList<BannerBean> = ArrayList()
            //取网站轮播图div
            val bannerBox = document.getElementsByClass("ck-slide-wrapper")
            //筛选div
            val targetElements = bannerBox.select("a[href]")
            targetElements.forEach {
                val title = it.select("img[src]").first().attr("alt")
                val image = it.select("img[src]").first().attr("src")
                val link = it.select("a[href]").first().attr("href")

                val bannerBean = BannerBean()
                bannerBean.bannerTitle = title
                bannerBean.bannerImage = image
                bannerBean.bannerLink = link

                bannerList.add(bannerBean)
            }
            return bannerList
        }

        /**
         * 解析ul标签下的图片集合
         */
        fun parseWallpaperUpdateData(document: Document): ArrayList<WallpaperBean> {
            val wallpaperList: ArrayList<WallpaperBean> = ArrayList()
            //取第三个ul内容
            val ulElement = document.select("ul[class]")[2]
            //筛选ul
            val targetElements = ulElement.select("a[href]")
            targetElements.forEach {
                val title = it.text()
                val image = it.select("img[data-original]").first().attr("data-original")
                //最新壁纸分类地址
                val url = it.select("a[href]").first().attr("href")

                val wallpaperBean = WallpaperBean()
                wallpaperBean.wallpaperTitle = title
                wallpaperBean.wallpaperImage = image
                wallpaperBean.wallpaperURL = url

                wallpaperList.add(wallpaperBean)
            }
            return wallpaperList
        }

        /**
         * 解析首页最新壁纸分类下连接的壁纸集合
         * */
        fun parseWallpaperData(document: Document): ArrayList<String> {
            val list = ArrayList<String>()
            //取第2个ul内容
            val ulElement = document.select("ul[id]")[1]
            //筛选ul
            val targetElements = ulElement.select("a[href]")
            targetElements.forEach {
                //得到每一张大图的html地址
                list.add(it.select("a[href]").first().attr("href"))
            }
            return list
        }

        /**
         * 解析【发现】数据
         */
        fun parseDiscoverData(document: Document): ArrayList<DiscoverBean> {
            val discoverList: ArrayList<DiscoverBean> = ArrayList()
            //取第四个ul内容
            val ulElement = document.select("ul[class]")[3]
            //筛选ul
            val liElements = ulElement.select("li")
            liElements.forEach { li ->
                val discoverBean = DiscoverBean()

                /**
                 * 解析第一个div
                 * */
                val titleDiv = li.select("div[class]")[0]
                //标题
                val title = titleDiv.select("img[src]").first().attr("title")
                //简介
                val synopsis = titleDiv.select("p").text()
                //大图
                val bitImg = titleDiv.select("img[data-original]").first().attr("data-original")
                //链接
                val link = titleDiv.select("a[class]").first().attr("href")

                /**
                 * 解析第二个div
                 * */
                val imageDiv = li.select("div[class]")[1]
                val imageElements = imageDiv.select("a[href]")
                val smallImageList: ArrayList<DiscoverBean.SmallImageBean> = ArrayList()
                imageElements.forEach { img ->
                    val smallImageBean = DiscoverBean.SmallImageBean()
                    smallImageBean.smallImage =
                        img.select("img[data-original]").first().attr("data-original")
                    smallImageList.add(smallImageBean)
                }

                discoverBean.discoverTitle = title
                discoverBean.discoverSynopsis = synopsis
                discoverBean.discoverURL = link
                discoverBean.bigImage = bitImg
                discoverBean.smallImages = smallImageList

                discoverList.add(discoverBean)
            }
            return discoverList
        }

        /**
         * 解析探索发现
         * */
        fun parseDiscoverDetailData(document: Document): ArrayList<WallpaperBean> {
            val wallpaperList: ArrayList<WallpaperBean> = ArrayList()
            //先选出目标内容的最外层div
            val mainDiv = document.getElementsByClass("list_cont list_cont2 w1180")
            mainDiv.forEach { main ->
                val ulElement = main.select("li")
                ulElement.forEach {
                    val title = it.text()
                    val image = it.select("img[data-original]").first().attr("data-original")
                    val link = it.select("a[href]").first().attr("href")

                    /**
                     * 去掉xxxx大全，重复数据太多
                     * http://www.win4000.com/mt/Pinky.html
                     *
                     * 保留如下类型数据
                     * http://www.win4000.com/meinv199524.html
                     * */
                    if (!link.contains("http://www.win4000.com/mt/")) {
                        val wallpaperBean = WallpaperBean()
                        wallpaperBean.wallpaperTitle = title
                        wallpaperBean.wallpaperImage = image
                        wallpaperBean.wallpaperURL = link

                        wallpaperList.add(wallpaperBean)
                    } else {
                        Log.d(Tag, "重复数据，不处理: $link")
                    }
                }
            }
            return wallpaperList
        }
    }
}