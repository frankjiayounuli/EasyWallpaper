package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.bean.HeadImageBean

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/7/1 15:10
 */
class HeadImageAdapter(ctx: Context, list: ArrayList<HeadImageBean>) : BaseAdapter() {

    private var context: Context = ctx
    private var beanList: ArrayList<HeadImageBean> = list
    private var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int = beanList.size

    override fun getItem(position: Int): Any = beanList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val itemViewHolder: ItemViewHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_head_image, null)
            itemViewHolder = ItemViewHolder(view)
            view.tag = itemViewHolder
        } else {
            view = convertView
            itemViewHolder = view.tag as ItemViewHolder
        }
        Glide.with(context).load(beanList[position].headImage).into(itemViewHolder.headImageView)
        return view
    }

    class ItemViewHolder(v: View) {
        var headImageView: ImageView = v.findViewById(R.id.headImageView)
    }
}