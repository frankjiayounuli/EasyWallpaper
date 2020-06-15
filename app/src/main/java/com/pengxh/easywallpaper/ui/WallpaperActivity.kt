package com.pengxh.easywallpaper.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.utils.BitmapCallBackListener
import com.pengxh.app.multilib.utils.ImageUtil
import com.pengxh.app.multilib.widget.gallery3d.BlurBitmapUtils
import com.pengxh.app.multilib.widget.gallery3d.CardScaleHelper
import com.pengxh.app.multilib.widget.gallery3d.ViewSwitchUtils
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.BigPictureAdapter
import com.pengxh.easywallpaper.utils.DocumentParseUtil
import kotlinx.android.synthetic.main.activity_wallpaper.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.*


/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/14 23:32
 */
class WallpaperActivity : BaseNormalActivity() {

    companion object {
        private const val Tag: String = "WallpaperActivity"
    }

    private val context: Context = this@WallpaperActivity
    private var wallpaperData: ArrayList<String> = ArrayList()
    private var mCardScaleHelper: CardScaleHelper = CardScaleHelper()
    private var mBlurRunnable: Runnable? = null

    override fun initLayoutView(): Int {
        return R.layout.activity_wallpaper
    }

    override fun initData() {
        ImmersionBar.with(this).init()

        val wallpaperURL = intent.getStringExtra("wallpaperURL")!!
        GlobalScope.launch(Dispatchers.Main) {
            val document = withContext(Dispatchers.IO) {
                Jsoup.connect(wallpaperURL).timeout(10 * 1000).get()
            }
            wallpaperData = DocumentParseUtil.parseWallpaperData(document)
            val bigPictureAdapter = BigPictureAdapter(context, wallpaperData)
            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            pictureGalleryView.layoutManager = linearLayoutManager
            pictureGalleryView.adapter = bigPictureAdapter
            //渲染背景
            mCardScaleHelper.currentItemPos = 0 //从第一张图片开始
            mCardScaleHelper.attachToRecyclerView(pictureGalleryView)
            pictureGalleryView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        notifyBackgroundChange()
                    }
                }
            })
            notifyBackgroundChange()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun notifyBackgroundChange() {
        var currentItemPos = mCardScaleHelper.currentItemPos
        if (currentItemPos >= wallpaperData.size) {
            currentItemPos = wallpaperData.size - 1
        }
        childNumberView.text = (currentItemPos + 1).toString() + "/" + wallpaperData.size //页码
        val imageHtml = wallpaperData[currentItemPos]
        GlobalScope.launch(Dispatchers.Main) {
            val childDocument = withContext(Dispatchers.IO) {
                Jsoup.connect(imageHtml).timeout(10 * 1000).get()
            }
            val bigImageURL = childDocument.select("img[class]").first().attr("src")
            ImageUtil.obtainBitmap(bigImageURL, object : BitmapCallBackListener {
                override fun onSuccess(bitmap: Bitmap?) {
                    pictureBlurView.removeCallbacks(mBlurRunnable)
                    mBlurRunnable = Runnable {
                        ViewSwitchUtils.startSwitchBackgroundAnim(
                            pictureBlurView,
                            BlurBitmapUtils.getBlurBitmap(pictureBlurView.context, bitmap, 15)
                        )
                    }
                    pictureBlurView.postDelayed(mBlurRunnable, 500)
                }

                override fun onFailure(t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    override fun initEvent() {

    }
}