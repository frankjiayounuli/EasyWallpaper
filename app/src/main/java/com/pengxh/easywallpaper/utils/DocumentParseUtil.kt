package com.pengxh.easywallpaper.utils

import com.google.gson.Gson
import com.pengxh.app.multilib.utils.SaveKeyValues
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
        fun parseBannerData(document: Document) {
            val bannerList: ArrayList<BannerBean> = ArrayList()
            //取网站轮播图div
            val slideBox = document.getElementsByClass("slidebox").first()
            //取第三个div内容
            val element = slideBox.getElementsByTag("div")[2]
            //筛选div
            val targetElements = element.select("a[href]")

            targetElements.forEach {
                /**
                 * http://www.win4000.com/zt/chahua.html
                 *
                 * /zt/chahua.html
                 * */
                val url = Constant.BaseURL + it.select("a[href]").first().attr("href")
                val image = it.select("img[src]").first().attr("src")

                val bannerBean = BannerBean()
                bannerBean.bannerURL = url
                bannerBean.bannerImage = image

                bannerList.add(bannerBean)
            }
            //将bannerMap数据存到sp
            SaveKeyValues.putValue("banner", Gson().toJson(bannerList))
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
            //取第2个ul内容
            val ulElement = document.select("ul[id]")[1]
            //筛选ul
            val targetElements = ulElement.select("a[href]")
            val list = ArrayList<String>()
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
    }
}