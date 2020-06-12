package com.pengxh.easywallpaper.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import com.gyf.immersionbar.ImmersionBar
import com.pengxh.app.multilib.base.BaseNormalActivity
import com.pengxh.easywallpaper.R
import kotlinx.android.synthetic.main.activity_splash.*

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

    override fun initLayoutView(): Int {
        return R.layout.activity_splash
    }

    override fun initData() {
        ImmersionBar.with(this).init()
        //开启爬虫抓取数据

    }

    override fun initEvent() {
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                startMainActivity()
            }

            override fun onTick(millisUntilFinished: Long) {
                splashTimerView.text = "跳过\r\r" + (1 + millisUntilFinished / 1000) + "s"
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