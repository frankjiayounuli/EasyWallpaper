package com.pengxh.easywallpaper.ui.fragment

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.aihook.alertview.library.AlertView
import com.aihook.alertview.library.OnItemClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.BaseFragment
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.FileUtil
import com.pengxh.easywallpaper.utils.HttpHelper
import com.pengxh.easywallpaper.utils.HttpListener
import kotlinx.android.synthetic.main.fragment_big_picture.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Document

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO 画廊大图
 * @date: 2020/6/18 17:32
 */
class BigPictureFragment(link: String) : BaseFragment() {

    companion object {
        private const val Tag: String = "BigPictureFragment"
    }

    private var bigImageLink = link
    private lateinit var bigImageUrl: String

    override fun initLayoutView(): Int = R.layout.fragment_big_picture

    override fun initData() {
        HttpHelper.getDocumentData(bigImageLink, object : HttpListener {
            override fun onSuccess(result: Document) {
                val e = result.getElementsByClass("pic-large").first()
                bigImageUrl = e.attr("url")
                //备用地址
                if (bigImageUrl == "") {
                    bigImageUrl = e.attr("src")
                }
                try {
                    Glide.with(context!!).load(bigImageUrl).into(photoView)
                } catch (e: NullPointerException) {
                    Log.e(Tag, ": $e")
                }
            }

            override fun onFailure(e: Exception) {

            }
        })
    }

    override fun initEvent() {
        photoView.setOnLongClickListener {
            AlertView("提示", "是否保存此张壁纸", "取消", arrayOf("确定"), null,
                context, AlertView.Style.Alert, OnItemClickListener { o, position ->
                    if (position == 0) {
                        GlobalScope.launch(Dispatchers.Main) {
                            val drawable = withContext(Dispatchers.IO) {
                                Glide.with(context!!).load(bigImageUrl)
                                    .apply(RequestOptions().centerCrop())
                                    .into(
                                        Target.SIZE_ORIGINAL,
                                        Target.SIZE_ORIGINAL
                                    ).get() as BitmapDrawable
                            }
                            if (FileUtil.saveWallpaper(context!!, drawable)) {
                                EasyToast.showToast("保存成功", EasyToast.SUCCESS)
                            } else {
                                EasyToast.showToast("保存失败", EasyToast.ERROR)
                            }
                        }
                    }
                }).setCancelable(false).show()
            false
        }
    }
}