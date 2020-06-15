package com.pengxh.easywallpaper.utils

import com.google.gson.Gson
import com.pengxh.app.multilib.utils.SaveKeyValues
import com.pengxh.easywallpaper.bean.BannerBean
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
    }
}