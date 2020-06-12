package com.pengxh.easywallpaper.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO 计算每个item之间的距离，均匀分布item
 * @date: 2020/6/12 12:56
 */
class RecyclerItemDecoration constructor(itemNumber: Int) : RecyclerView.ItemDecoration() {

    private var number = itemNumber

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemWidth = parent.resources.displayMetrics.widthPixels / number

        when (parent.indexOfChild(view)) {
            0 -> {
                outRect.left = itemWidth / (number - 1)
                outRect.right = itemWidth / (number + 1)
            }
            3 -> {
                outRect.left = itemWidth / (number + 1)
                outRect.right = itemWidth / (number - 1)
            }
            else -> {
                outRect.left = itemWidth / (number + 1)
                outRect.right = itemWidth / (number + 1)
            }
        }
    }
}