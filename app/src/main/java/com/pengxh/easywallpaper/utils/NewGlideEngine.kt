package com.pengxh.easywallpaper.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.pengxh.easywallpaper.R
import com.zhihu.matisse.engine.ImageEngine

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO 最新版的Glide改动较大，需要自定义一个图片引擎，不然回报NoSuchMethodError。//https://blog.csdn.net/f987002856/article/details/77990908
 * @date: 2020/4/21 14:55
 */
class NewGlideEngine : ImageEngine {
    override fun loadImage(
        context: Context?,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView?,
        uri: Uri?
    ) {
        val options = RequestOptions()
            .centerCrop()
            .override(resizeX, resizeY)
            .priority(Priority.HIGH)
        Glide.with(context!!).load(uri).apply(options).into(imageView!!)
    }

    override fun loadGifImage(
        context: Context?,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView?,
        uri: Uri?
    ) {
        val options = RequestOptions()
            .centerCrop()
            .override(resizeX, resizeY)
            .priority(Priority.HIGH)
        Glide.with(context!!).asGif().load(uri).apply(options).into(imageView!!)
    }

    override fun supportAnimatedGif(): Boolean {
        return true
    }

    override fun loadGifThumbnail(
        context: Context?,
        resize: Int,
        placeholder: Drawable?,
        imageView: ImageView?,
        uri: Uri?
    ) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(placeholder)//这里可自己添加占位图
            .error(R.drawable.ic_empty)//这里可自己添加出错图
            .override(resize, resize)
        Glide.with(context!!).asBitmap().load(uri).apply(options).into(imageView!!)
    }

    override fun loadThumbnail(
        context: Context?,
        resize: Int,
        placeholder: Drawable?,
        imageView: ImageView?,
        uri: Uri?
    ) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(placeholder)//这里可自己添加占位图
            .error(R.drawable.ic_empty)//这里可自己添加出错图
            .override(resize, resize)
        Glide.with(context!!).asBitmap().load(uri).apply(options).into(imageView!!)
    }
}