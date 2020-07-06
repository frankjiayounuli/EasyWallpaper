package com.pengxh.easywallpaper.ui.fragment

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.aihook.alertview.library.AlertView
import com.aihook.alertview.library.OnItemClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pengxh.app.multilib.utils.BitmapCallBackListener
import com.pengxh.app.multilib.utils.DensityUtil
import com.pengxh.app.multilib.utils.ImageUtil
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.BaseFragment
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.FileUtil
import com.pengxh.easywallpaper.utils.HTMLParseUtil
import com.pengxh.easywallpaper.utils.HttpHelper
import com.pengxh.easywallpaper.utils.HttpListener
import kotlinx.android.synthetic.main.fragment_big_picture.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Document
import java.io.IOException


/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO 画廊大图
 * @date: 2020/6/18 17:32
 */
class BigPictureFragment : BaseFragment() {

    companion object {
        private const val Tag: String = "BigPictureFragment"
        private val actionArray = arrayOf("保存到手机", "设置为壁纸")

        fun newInstance(link: String): BigPictureFragment? {
            val fragment = BigPictureFragment()
            val bundle = Bundle()
            bundle.putString("pageLink", link)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var bigImageUrl: String

    override fun initLayoutView(): Int = R.layout.fragment_big_picture

    override fun initData() {
        arguments!!.getString("pageLink")?.let {
            HttpHelper.getDocumentData(it, object : HttpListener {
                override fun onSuccess(result: Document) {
                    bigImageUrl = HTMLParseUtil.parseWallpaperURL(result)

                    val imageBuilder = context?.let { ctx ->
                        Glide.with(ctx).load(bigImageUrl)
                    }

                    try {
                        imageBuilder?.into(bigImageView)
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(e: Exception) {
                    e.printStackTrace()
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initEvent() {
        bigImageView.setOnLongClickListener {
            AlertView("请选择", null, "取消", null, actionArray, context, AlertView.Style.ActionSheet,
                OnItemClickListener { o, position ->
                    when (position) {
                        -1 -> {
                            Log.d(Tag, "onItemClick: 取消按钮，无需理会")
                        }
                        0 -> {
                            GlobalScope.launch(Dispatchers.Main) {
                                val drawable = withContext(Dispatchers.IO) {
                                    Glide.with(context!!).load(bigImageUrl)
                                        .apply(RequestOptions().centerCrop())
                                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .get() as BitmapDrawable
                                }
                                if (FileUtil.saveWallpaper(context!!, drawable)) {
                                    EasyToast.showToast("保存成功", EasyToast.SUCCESS)
                                } else {
                                    EasyToast.showToast("保存失败", EasyToast.ERROR)
                                }
                            }
                        }
                        1 -> {
                            ImageUtil.obtainBitmap(bigImageUrl, object : BitmapCallBackListener {
                                override fun onSuccess(bitmap: Bitmap?) {
                                    val wallpaperManager = WallpaperManager.getInstance(context)
                                    try {
                                        val width = DensityUtil.getScreenHeight(context)
                                        val height = DensityUtil.getScreenHeight(context)
                                        wallpaperManager.suggestDesiredDimensions(width, height)
                                        wallpaperManager.setBitmap(bitmap)
                                        EasyToast.showToast("壁纸设置成功", EasyToast.SUCCESS)
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }

                                override fun onFailure(t: Throwable?) {
                                    t!!.printStackTrace()
                                }
                            })
                        }
                    }
                }).setCancelable(false).show()
            false
        }
    }
}