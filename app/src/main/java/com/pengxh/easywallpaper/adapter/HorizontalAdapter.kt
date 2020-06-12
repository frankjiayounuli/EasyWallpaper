package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.Constant

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/12 10:49
 */
class HorizontalAdapter(mContext: Context) :
    RecyclerView.Adapter<HorizontalAdapter.ItemViewHolder>() {

    private var context = mContext
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = Constant.items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        Glide.with(context).load(Constant.itemImages[position]).into(holder.itemImage)
        holder.itemTitle.text = Constant.items[position]

        // 点击事件
        holder.itemLayout.setOnClickListener {
            itemClickListener!!.onItemClickListener(position)
        }
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var itemLayout: LinearLayout = itemView!!.findViewById(R.id.itemLayout)
        var itemImage: ImageView = itemView!!.findViewById(R.id.itemImage)
        var itemTitle: TextView = itemView!!.findViewById(R.id.itemTitle)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClickListener(position: Int)
    }
}