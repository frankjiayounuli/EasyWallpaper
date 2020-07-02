package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.bean.DiscoverBean
import com.pengxh.easywallpaper.utils.OnItemClickListener
import java.util.*

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/16 10:11
 */
class DiscoverAdapter(ctx: Context, list: ArrayList<DiscoverBean>) :
    RecyclerView.Adapter<DiscoverAdapter.ItemViewHolder>() {

    private var context: Context = ctx
    private var beanList: ArrayList<DiscoverBean> = list
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discover, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val discoverBean = beanList[position]
        holder.discoverTitle.text = discoverBean.discoverTitle
        holder.discoverSynopsis.text = discoverBean.discoverSynopsis
        Glide.with(context).load(discoverBean.bigImage).into(holder.bigImageView)

        //小图绑定
        holder.smallGridView.adapter = SmallImageAdapter(context, discoverBean.smallImages)

        //点击事件
        holder.itemLayout.setOnClickListener {
            itemClickListener!!.onItemClickListener(position)
        }
    }

    override fun getItemCount(): Int = beanList.size

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var itemLayout: ConstraintLayout = itemView!!.findViewById(R.id.itemLayout)
        var discoverTitle: TextView = itemView!!.findViewById(R.id.discoverTitle)
        var discoverSynopsis: TextView = itemView!!.findViewById(R.id.discoverSynopsis)
        var bigImageView: ImageView = itemView!!.findViewById(R.id.bigImageView)
        var smallGridView: GridView = itemView!!.findViewById(R.id.smallGridView)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }
}