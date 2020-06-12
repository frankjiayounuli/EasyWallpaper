package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.bean.BannerBean
import com.youth.banner.adapter.BannerAdapter


/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/12 9:45
 */
class BannerImageAdapter(ctx: Context, data: List<BannerBean>?) :
    BannerAdapter<BannerBean, BannerImageAdapter.BannerViewHolder>(data) {

    private var context = ctx
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannerViewHolder {
        val imageView = ImageView(context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: BannerBean,
        position: Int,
        size: Int
    ) {
        //图片是http的，9.0以上需要android:usesCleartextTraffic="true"
        Glide.with(context).load(data.bannerImage)
            .apply(RequestOptions().placeholder(R.drawable.ic_empty))
            .into(holder.imageView)
        holder.imageView.setOnClickListener {
            itemClickListener!!.onItemClickListener(position)
        }
    }

    inner class BannerViewHolder(var imageView: ImageView) :
        RecyclerView.ViewHolder(imageView)

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClickListener(position: Int)
    }
}