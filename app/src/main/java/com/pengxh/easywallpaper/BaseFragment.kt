package com.pengxh.easywallpaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/11 16:36
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflater.inflate(initLayoutView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initEvent()
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    protected abstract fun initLayoutView(): Int

    /**
     * 数据初始化操作
     */
    protected abstract fun initData()

    /**
     * 业务逻辑操作
     */
    protected abstract fun initEvent()
}