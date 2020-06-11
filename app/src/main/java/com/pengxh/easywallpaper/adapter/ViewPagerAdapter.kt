package com.pengxh.easywallpaper.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 15:22
 */
class ViewPagerAdapter(list: ArrayList<Fragment>, manager: FragmentManager) :
    FragmentPagerAdapter(manager) {

    private var pageList: List<Fragment> = list

    override fun getItem(position: Int): Fragment = pageList[position]

    override fun getCount(): Int = pageList.size
}