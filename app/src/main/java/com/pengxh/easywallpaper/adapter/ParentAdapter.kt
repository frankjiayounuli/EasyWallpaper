package com.pengxh.easywallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.bean.CategoryBean

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/19 15:55
 */
class ParentAdapter(ctx: Context, list: ArrayList<CategoryBean>) : BaseAdapter() {

    private var context = ctx
    private val beanList = list
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
            view = inflater.inflate(R.layout.item_parent, null)
            itemViewHolder = ItemViewHolder(view)
            view.tag = itemViewHolder
        } else {
            view = convertView
            itemViewHolder = view.tag as ItemViewHolder
        }
        itemViewHolder.categoryName.text = beanList[position].categoryName
        return view
    }

    class ItemViewHolder(v: View) {
        var categoryName: TextView = v.findViewById(R.id.categoryName)
    }
}