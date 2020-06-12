package com.pengxh.easywallpaper.utils

import com.google.gson.Gson
import com.pengxh.app.multilib.utils.SaveKeyValues
import com.pengxh.easywallpaper.bean.BannerBean
import org.jsoup.nodes.Document
import java.util.*

/**
 * @description: TODO document数据解析
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/12 23:42
 */
class DocumentParseUtil {
    companion object {
        private const val Tag: String = "DocumentParseUtil"

        fun parseBannerData(document: Document) {
            //取网站轮播图div
            val slideBox = document.getElementsByClass("slidebox").first()
            //取第三个div内容
            val element = slideBox.getElementsByTag("div")[2]
            //筛选div
            val targetElements = element.select("a[href]")

            val bannerList: ArrayList<BannerBean> = ArrayList()
            for (e in targetElements) {
                /**
                 * http://www.win4000.com/zt/chahua.html
                 *
                 * /zt/chahua.html
                 * */
                val url = Constant.BaseURL + e.select("a[href]").first().attr("href")
                val image = e.select("img[src]").first().attr("src")

                val bannerBean = BannerBean()
                bannerBean.bannerURL = url
                bannerBean.bannerImage = image

                bannerList.add(bannerBean)
            }
            //将bannerMap数据存到sp
            SaveKeyValues.putValue("banner", Gson().toJson(bannerList))
        }
    }
}