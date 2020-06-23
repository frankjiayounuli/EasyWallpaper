package com.pengxh.easywallpaper.utils

import android.util.Log
import androidx.annotation.NonNull
import com.pengxh.app.multilib.widget.EasyToast
import com.pengxh.easywallpaper.bean.MailBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/23 23:01
 */
class SendMailUtil {
    companion object {
        private const val Tag: String = "SendMailUtil"

        fun sendTextEmail(emailMessage: String) {
            Log.d(Tag, "sendTextEmail: ")
            GlobalScope.launch(Dispatchers.Main) {
                val result = withContext(Dispatchers.IO) {
                    MailSender().sendTextMail(createMail(emailMessage))
                }
                if (result) {
                    EasyToast.showToast("问题反馈成功", EasyToast.SUCCESS)
                } else {
                    EasyToast.showToast("问题反馈失败", EasyToast.ERROR)
                }
            }
        }

        fun sendAttachFileEmail(emailMessage: String, filePath: String?) {
            Log.d(Tag, "sendAttachFileEmail: ")
            val file = File(filePath!!)
            GlobalScope.launch(Dispatchers.Main) {
                val result = withContext(Dispatchers.IO) {
                    MailSender().sendAccessoryMail(createAttachMail(emailMessage, file))
                }
                if (result) {
                    EasyToast.showToast("问题反馈成功", EasyToast.SUCCESS)
                } else {
                    EasyToast.showToast("问题反馈失败", EasyToast.ERROR)
                }
            }
        }

        @NonNull
        fun createMail(emailMessage: String): MailBean {
            val mailBean = MailBean()
            mailBean.mailServerHost = "smtp.qq.com" //发送方邮箱服务器
            mailBean.mailServerPort = "587" //发送方邮箱端口号
            mailBean.validate = true
            mailBean.userName = "290677893@qq.com" // 发送者邮箱地址
            mailBean.password = "gqvwykjvpnvfbjid" //邮箱授权码，不是密码
            mailBean.content = emailMessage // 邮件文本
            return mailBean
        }

        @NonNull
        fun createAttachMail(emailMessage: String, file: File): MailBean {
            Log.d(Tag, "createAttachMail: 创建邮件实体开始")
            val mailBean = MailBean()
            mailBean.mailServerHost = "smtp.qq.com" //发送方邮箱服务器
            mailBean.mailServerPort = "587" //发送方邮箱端口号
            mailBean.validate = true
            mailBean.userName = "290677893@qq.com" // 发送者邮箱地址
            mailBean.password = "gqvwykjvpnvfbjid" //邮箱授权码，不是密码
            mailBean.content = emailMessage
            mailBean.attachFile = file
            Log.d(Tag, "createAttachMail: 创建邮件实体结束")
            return mailBean
        }
    }
}