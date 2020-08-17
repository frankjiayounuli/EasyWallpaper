# EasyWallpaper
高清手机壁纸
这款软件是我写的第一个完整的Kotlin项目，而且数据采用的是爬虫爬取的数据，要感谢[目标网址：美桌](http://www.win4000.com/mobile.html)，让我有数据可用，所以，本项目仅能用于学习，严禁商用或者其他盈利用途！
附上两个核心工具类，爬虫和Kotlin协程
###Kotlin协程工具类
```Kotlin
/**
 * @description: TODO 多协程
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/14 16:26
 */
class HttpHelper {
    companion object {
        private const val Tag: String = "HttpHelper"

        private fun createConnection(url: String): Connection = Jsoup.connect(url)
            .userAgent(obtainAgent())
            .timeout(30 * 1000)
            .ignoreHttpErrors(true)

        private fun obtainAgent(): String = Constant.UA[Random().nextInt(15)]

        private fun isNetworkAvailable(): Boolean {
            val connectivityManager = BaseApplication.instance()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // 获取所有NetworkInfo对象
            val networkInfo = connectivityManager.allNetworkInfo
            if (networkInfo.isNotEmpty()) {
                for (i in networkInfo.indices) if (networkInfo[i].state == NetworkInfo.State.CONNECTED) return true // 存在可用的网络连接
            }
            return false
        }

        /**
         * 获取最新壁纸
         * */
        fun getWallpaperUpdate(pageNumber: Int, listener: HttpListener) {
            val url = Constant.WallpaperUpdateURL.replace("index", pageNumber.toString(), true)
            if (isNetworkAvailable()) {
                GlobalScope.launch(Dispatchers.Main) {
                    val status = withContext(Dispatchers.IO) {
                        createConnection(url).execute().statusCode()
                    }
                    if (status == 200) {
                        listener.onSuccess(withContext(Dispatchers.IO) {
                            createConnection(url).get()
                        })
                    } else {
                        listener.onFailure(IndexOutOfBoundsException("IndexOutOfBoundsException"))
                    }
                }
            } else {
                listener.onFailure(Exception("SocketTimeoutException"))
            }
        }

        /**
         * 加载更多壁纸数据
         * */
        fun loadMoreWallpaper(pageNumber: Int, firstLink: String, listener: HttpListener) {
            if (isNetworkAvailable()) {
                Log.d(Tag, "loadMoreWallpaper: $firstLink")
                val newLink = firstLink.replace("1.html", "$pageNumber.html", true)
                GlobalScope.launch(Dispatchers.Main) {
                    val status = withContext(Dispatchers.IO) {
                        createConnection(newLink).execute().statusCode()
                    }
                    if (status == 200) {
                        listener.onSuccess(withContext(Dispatchers.IO) {
                            createConnection(newLink).get()
                        })
                    } else {
                        listener.onFailure(IndexOutOfBoundsException("IndexOutOfBoundsException"))
                    }
                }
            } else {
                EasyToast.showToast("哎呀，网络似乎断开了~", EasyToast.ERROR)
            }
        }

        /**
         * 加载更多写真数据
         * */
        fun loadMoreStarWallpaper(pageNumber: Int, firstLink: String, listener: HttpListener) {
            if (isNetworkAvailable()) {
                val newLink = firstLink.replace(".html", "_$pageNumber.html", true)
                GlobalScope.launch(Dispatchers.Main) {
                    val status = withContext(Dispatchers.IO) {
                        createConnection(newLink).execute().statusCode()
                    }
                    if (status == 200) {
                        listener.onSuccess(withContext(Dispatchers.IO) {
                            createConnection(newLink).get()
                        })
                    } else {
                        listener.onFailure(IndexOutOfBoundsException("IndexOutOfBoundsException"))
                    }
                }
            } else {
                EasyToast.showToast("哎呀，网络似乎断开了~", EasyToast.ERROR)
            }
        }

        /**
         * 获取探索发现数据
         * */
        fun getDiscoverData(pageNumber: Int, listener: HttpListener) {
            if (isNetworkAvailable()) {
                val url: String = if (pageNumber == 1) {
                    Constant.DiscoverURL
                } else {
                    Constant.DiscoverURL.replace("index", "index$pageNumber", true)
                }
                GlobalScope.launch(Dispatchers.Main) {
                    val status = withContext(Dispatchers.IO) {
                        createConnection(url).execute().statusCode()
                    }
                    if (status == 200) {
                        listener.onSuccess(withContext(Dispatchers.IO) {
                            createConnection(url).get()
                        })
                    } else {
                        listener.onFailure(IndexOutOfBoundsException("IndexOutOfBoundsException"))
                    }
                }
            } else {
                listener.onFailure(Exception("SocketTimeoutException"))
            }
        }

        /**
         * 抓取精选头像列表源数据
         * */
        fun getHeadImageData(pageNumber: Int, listener: HttpListener) {
            if (isNetworkAvailable()) {
                val url: String = if (pageNumber == 1) {
                    Constant.HeadImageURL
                } else {
                    Constant.HeadImageURL + "index_" + pageNumber + ".html"
                }
                Log.d(Tag, "头像列表链接地址: $url")
                GlobalScope.launch(Dispatchers.Main) {
                    val status = withContext(Dispatchers.IO) {
                        createConnection(url).execute().statusCode()
                    }
                    if (status == 200) {
                        listener.onSuccess(withContext(Dispatchers.IO) {
                            createConnection(url).get()
                        })
                    } else {
                        listener.onFailure(IndexOutOfBoundsException("IndexOutOfBoundsException"))
                    }
                }
            } else {
                listener.onFailure(Exception("SocketTimeoutException"))
            }
        }

        fun getDocumentData(link: String, listener: HttpListener) {
            if (isNetworkAvailable()) {
                Log.d(Tag, "抓取地址: $link")
                GlobalScope.launch(Dispatchers.Main) {
                    val status = withContext(Dispatchers.IO) {
                        createConnection(link).execute().statusCode()
                    }
                    if (status == 200) {
                        listener.onSuccess(withContext(Dispatchers.IO) {
                            createConnection(link).get()
                        })
                    } else {
                        listener.onFailure(IndexOutOfBoundsException("IndexOutOfBoundsException"))
                    }
                }
            } else {
                EasyToast.showToast("哎呀，网络似乎断开了~", EasyToast.ERROR)
            }
        }
    }
}
```
###Jsoup爬虫工具类
```Kotlin
/**
 * @description: TODO html数据解析
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/12 23:42
 */
class HTMLParseUtil {
    companion object {
        private const val Tag: String = "HTMLParseUtil"

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
            val elements = document.select("ul[class]")[2].select("li")
            elements.forEach {
                list.add(it.select("a[href]").first().attr("href"))
            }
            return list
        }

        /**
         * 高清壁纸大图地址
         * */
        fun parseWallpaperURL(document: Document): String {
            val e = document.getElementsByClass("pic-large").first()
            var bigImageUrl = e.attr("url")
            //备用地址
            if (bigImageUrl == "") {
                bigImageUrl = e.attr("src")
            }
            return bigImageUrl
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
                    smallImageBean.smallImage = img.select("img[data-original]").first().attr("data-original")
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
                     * 去掉mt，重复数据太多
                     * http://www.win4000.com/mt/Pinky.html
                     *
                     * 去掉meinvtag，重复数据太多
                     * http://www.win4000.com/meinvtag43591.html
                     *
                     * 保留如下类型数据
                     * http://www.win4000.com/meinv199524.html
                     * */
                    if (!link.contains("/mt/")) {
                        if (!link.contains("/meinvtag")) {
                            val wallpaperBean = WallpaperBean()
                            wallpaperBean.wallpaperTitle = title
                            wallpaperBean.wallpaperImage = image
                            wallpaperBean.wallpaperURL = link

                            wallpaperList.add(wallpaperBean)
                        }
                    }
                }
            }
            return wallpaperList
        }

        /**
         * 解析壁纸分类数据
         * */
        fun parseCategoryData(document: Document): ArrayList<CategoryBean> {
            val categoryList: ArrayList<CategoryBean> = ArrayList()
            val elements = document
                .getElementsByClass("product_query").first()
                .getElementsByClass("cont1").first()
                .select("a[href]")
            //去掉第一个
            for (i in elements.indices) {
                if (i == 0) continue
                val element = elements[i]

                val categoryBean = CategoryBean()
                categoryBean.categoryName = element.text()
                categoryBean.categoryLink = element.select("a[href]").first().attr("href")
                categoryList.add(categoryBean)
            }
            return categoryList
        }

        /**
         * 解析明星分类数据
         * */
        fun parseStarData(document: Document): ArrayList<CategoryBean> {
            val categoryList: ArrayList<CategoryBean> = ArrayList()
            val elements = document
                .getElementsByClass("cb_cont").first()
                .select("a[href]")
            //替换第一个
            for (i in elements.indices) {
                if (i == 0) {
                    //抓取顶部圆头像
                    val categoryBean = CategoryBean()
                    categoryBean.categoryName = "推荐"
                    categoryBean.categoryLink = "http://www.win4000.com/mt/star.html"
                    categoryList.add(0, categoryBean)
                } else {
                    val element = elements[i]
                    val categoryBean = CategoryBean()
                    categoryBean.categoryName = element.text()
                    categoryBean.categoryLink = element.select("a[href]").first().attr("href")
                    categoryList.add(i, categoryBean)
                }
            }
            return categoryList
        }

        /**
         * 解析顶部圆形头像数据
         * */
        fun parseCircleImageData(document: Document): ArrayList<WallpaperBean> {
            val circleImageList: ArrayList<WallpaperBean> = ArrayList()
            val elements = document.getElementsByClass("nr_zt w1180").first().select("li")
            elements.forEach {
                val title = it.text()
                val image = it.select("img[src]").first().attr("src")
                val link = Constant.BaseURL + it.select("a[href]").first().attr("href")

                val wallpaperBean = WallpaperBean()
                wallpaperBean.wallpaperTitle = title
                wallpaperBean.wallpaperImage = image
                wallpaperBean.wallpaperURL = link

                circleImageList.add(wallpaperBean)
            }
            return circleImageList
        }

        /**
         * 解析搜索结果
         * */
        fun parseSearchData(document: Document): ArrayList<WallpaperBean> {
            val searchList: ArrayList<WallpaperBean> = ArrayList()
            val elements = document.getElementsByClass("tab_box").first().select("li")
            elements.forEach {
                val title = it.text()
                val image = it.select("img[data-original]").first().attr("data-original")
                val link = it.select("a[href]").first().attr("href")

                val wallpaperBean = WallpaperBean()
                wallpaperBean.wallpaperTitle = title
                wallpaperBean.wallpaperImage = image
                wallpaperBean.wallpaperURL = link

                searchList.add(wallpaperBean)
            }
            return searchList
        }

        /**
         * 解析明星写真页面壁纸数据
         * */
        fun parseStarPersonalData(document: Document): ArrayList<WallpaperBean> {
            val starList: ArrayList<WallpaperBean> = ArrayList()
            val elements = document.select("ul[class]")[2].select("li")
            elements.forEach {
                val title = it.text()
                val image = it.select("img[src]").first().attr("src")
                val link = it.select("a[href]").first().attr("href")

                val wallpaperBean = WallpaperBean()
                wallpaperBean.wallpaperTitle = title
                wallpaperBean.wallpaperImage = image
                wallpaperBean.wallpaperURL = link

                starList.add(wallpaperBean)
            }
            return starList
        }

        /**
         * 解析精选头像数据
         * */
        fun parseHeadImageData(document: Document): ArrayList<HeadImageBean> {
            val list = ArrayList<HeadImageBean>()
            val elements = document.getElementsByClass("g-gxlist-imgbox").select("li")
            elements.forEach {
                val image = it.select("img[src]").first().attr("src")
                //大图页面链接
                val link = it.select("a[href]").first().attr("href")

                val headImageBean = HeadImageBean()
                headImageBean.headImage = image
                headImageBean.headImageLink = link

                list.add(headImageBean)
            }
            return list
        }

        /**
         * 获取头像大图地址
         * */
        fun parseHeadImageURL(document: Document): String = document
            .getElementsByClass("img-list4").first()
            .select("img").attr("src")
    }
}
```
