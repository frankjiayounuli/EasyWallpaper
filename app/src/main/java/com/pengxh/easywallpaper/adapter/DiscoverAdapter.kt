package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_discover, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val discoverBean = beanList[position]
        holder.discoverTitle.text = discoverBean.discoverTitle
        holder.discoverSynopsis.text = discoverBean.discoverSynopsis
        Glide.with(context).load(discoverBean.bigImage).into(holder.bigImageView)

        //小图绑定
        Glide.with(context).load(discoverBean.smallImages[0].smallImage).into(holder.smallView1)
        Glide.with(context).load(discoverBean.smallImages[1].smallImage).into(holder.smallView2)
        Glide.with(context).load(discoverBean.smallImages[2].smallImage).into(holder.smallView3)
        Glide.with(context).load(discoverBean.smallImages[3].smallImage).into(holder.smallView4)

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

        var smallView1: ImageView = itemView!!.findViewById(R.id.smallView1)
        var smallView2: ImageView = itemView!!.findViewById(R.id.smallView2)
        var smallView3: ImageView = itemView!!.findViewById(R.id.smallView3)
        var smallView4: ImageView = itemView!!.findViewById(R.id.smallView4)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }
}