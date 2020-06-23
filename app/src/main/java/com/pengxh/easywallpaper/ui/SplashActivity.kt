package com.pengxh.easywallpaper.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.app.multilib.utils.SaveKeyValues
import com.pengxh.easywallpaper.R
import com.pengxh.easywallpaper.utils.HTMLParseUtil
import com.pengxh.easywallpaper.utils.HttpHelper
import com.pengxh.easywallpaper.utils.HttpListener
import kotlinx.android.synthetic.main.activity_splash.*
import org.jsoup.nodes.Document

/**
 * @description: TODO 此页面可以加载基本数据
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/12 22:37
 */
@SuppressLint("SetTextI18n")
class SplashActivity : BaseNormalActivity() {

    companion object {
        private const val Tag: String = "SplashActivity"
    }

    private lateinit var countDownTimer: CountDownTimer

    override fun initLayoutView(): Int = R.layout.activity_splash

    override fun initData() {
        ImmersionBar.with(this).init()
        HttpHelper.getWallpaperUpdate(1, object : HttpListener {
            override fun onSuccess(result: Document) {
                //默认加载第一页数据
                val wallpaperUpdateData = HTMLParseUtil.parseWallpaperUpdateData(result)
                //将最新壁纸数据存sp
                SaveKeyValues.putValue("wallpaperData", Gson().toJson(wallpaperUpdateData))
                //取最新壁纸数据的第一个作为闪屏
                val wallpaperURL = wallpaperUpdateData[0].wallpaperURL
                HttpHelper.getDocumentData(wallpaperURL, object : HttpListener {
                    override fun onSuccess(result: Document) {
                        val e = result.getElementsByClass("pic-large").first()
                        var bigImageUrl = e.attr("url")
                        //备用地址
                        if (bigImageUrl == "") {
                            bigImageUrl = e.attr("src")
                        }
                        Glide.with(this@SplashActivity).load(bigImageUrl).into(splashImageView)
                    }

                    override fun onFailure(e: Exception) {

                    }
                })
            }

            override fun onFailure(e: Exception) {

            }
        })
    }

    override fun initEvent() {
        countDownTimer = object : CountDownTimer(6000, 1000) {
            override fun onFinish() {
                startMainActivity()
            }

            override fun onTick(millisUntilFinished: Long) {
                splashTimerView.text = "点击跳过\r\r" + millisUntilFinished / 1000
            }
        }
        //启动倒计时
        countDownTimer.start()

        splashTimerView.setOnClickListener {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}