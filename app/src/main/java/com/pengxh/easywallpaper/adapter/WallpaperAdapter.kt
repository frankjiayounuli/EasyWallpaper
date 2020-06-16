package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
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
class WallpaperAdapter(mContext: Context, list: ArrayList<WallpaperBean>) :
    RecyclerView.Adapter<WallpaperAdapter.ItemViewHolder>() {

    private var context = mContext
    private var dataBeans = list
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_wallpaper, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = dataBeans.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val wallpaperBean = dataBeans[position]

        Glide.with(context).load(wallpaperBean.wallpaperImage).into(holder.itemImage)
        holder.itemTitle.text = wallpaperBean.wallpaperTitle

        // 点击事件
        holder.itemLayout.setOnClickListener {
            itemClickListener!!.onItemClickListener(position)
        }
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var itemLayout: CardView = itemView!!.findViewById(R.id.itemLayout)
        var itemImage: ImageView = itemView!!.findViewById(R.id.itemImage)
        var itemTitle: TextView = itemView!!.findViewById(R.id.itemTitle)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }
}