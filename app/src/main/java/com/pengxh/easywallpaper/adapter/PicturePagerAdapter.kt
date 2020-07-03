package com.pengxh.easywallpaper.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/5/18 16:50
 */
class PicturePagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    private val mFragmentList: MutableList<Fragment> = CopyOnWriteArrayList()

    fun addPage(index: Int, fragment: Fragment) {
        mFragmentList.add(index, fragment)
        notifyDataSetChanged()
    }

    fun addPage(fragment: Fragment) {
        mFragmentList.add(fragment)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    /**
     * 返回值有三种，
     * POSITION_UNCHANGED  默认值，位置没有改变
     * POSITION_NONE       item已经不存在
     * position            item新的位置
     * 当position发生改变时这个方法应该返回改变后的位置，以便页面刷新。
     */
    override fun getItemPosition(`object`: Any): Int {
        if (`object` is Fragment) {
            val index = mFragmentList.indexOf(`object`)
            return if (index != -1) {
                index
            } else {
                PagerAdapter.POSITION_NONE
            }
        }
        return super.getItemPosition(`object`)
    }

    override fun getItemId(position: Int): Long {
        return mFragmentList[position].hashCode().toLong()
    }
}