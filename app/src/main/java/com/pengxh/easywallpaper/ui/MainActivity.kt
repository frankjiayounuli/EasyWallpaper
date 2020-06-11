package com.pengxh.easywallpaper.ui

import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.pengxh.app.multilib.base.DoubleClickExitActivity
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.adapter.ViewPagerAdapter
import com.pengxh.easywallpaper.ui.fragment.SearchFragment
import com.pengxh.easywallpaper.ui.fragment.SettingsFragment
import com.pengxh.easywallpaper.ui.fragment.WallPaperFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DoubleClickExitActivity() {

    companion object {
        private const val Tag = "MainActivity"
    }

    private var menuItem: MenuItem? = null
    private lateinit var fragmentList: ArrayList<Fragment>

    override fun initLayoutView(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        fragmentList = ArrayList()
        fragmentList.add(WallPaperFragment())
        fragmentList.add(SearchFragment())
        fragmentList.add(SettingsFragment())
    }

    override fun initEvent() {
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_wallpaper -> mainViewPager.currentItem = 0
                R.id.nav_search -> mainViewPager.currentItem = 1
                R.id.nav_settings -> mainViewPager.currentItem = 2
            }
            false
        }
        val pagerAdapter: ViewPagerAdapter =
            ViewPagerAdapter(fragmentList, supportFragmentManager)
        mainViewPager.adapter = pagerAdapter
        mainViewPager.offscreenPageLimit = fragmentList.size//缓存页数
        mainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (menuItem != null) {
                    menuItem!!.isChecked = false
                } else {
                    bottomNavigation.menu.getItem(0).isChecked = false
                }
                menuItem = bottomNavigation.menu.getItem(position)
                menuItem!!.isChecked = true
            }
        })
    }
}
