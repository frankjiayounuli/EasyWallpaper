package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.bean.WallpaperBean
import com.pengxh.easywallpaper.utils.OnItemClickListener

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/14 17:37
 */
class CircleImageAdapter(mContext: Context, list: ArrayList<WallpaperBean>) :
    RecyclerView.Adapter<CircleImageAdapter.ItemViewHolder>() {

    private var context = mContext
    private var dataBeans = list
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_circle_image, parent, false))
    }

    override fun getItemCount(): Int = dataBeans.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val wallpaperBean = dataBeans[position]

        Glide.with(context).load(wallpaperBean.wallpaperImage).into(holder.starCircleImage)
        holder.starName.text = wallpaperBean.wallpaperTitle

        // 点击事件
        holder.itemLayout.setOnClickListener {
            itemClickListener!!.onItemClickListener(position)
        }
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var itemLayout: RelativeLayout = itemView!!.findViewById(R.id.itemLayout)
        var starCircleImage: ImageView = itemView!!.findViewById(R.id.starCircleImage)
        var starName: TextView = itemView!!.findViewById(R.id.starName)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }
}