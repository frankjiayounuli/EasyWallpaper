package com.pengxh.easywallpaper.bean

import java.io.File
import java.util.*

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/23 22:49
 */
class EmailBean {
    // 发送邮件的服务器的IP和端口
    lateinit var mailServerHost: String
    lateinit var mailServerPort: String

    // 邮件发送者的地址
    lateinit var fromAddress: String

    // 邮件接收者的地址
    lateinit var toAddress: String

    // 登陆邮件发送服务器的用户名和密码
    lateinit var userName: String
    lateinit var password: String

    // 是否需要身份验证
    var validate = false

    // 邮件主题
    lateinit var subject: String

    // 邮件的文本内容
    lateinit var content: String

    // 邮件的附件
    lateinit var attachFile: File

    /**
     * 获得邮件会话属性
     */
    fun getProperties(): Properties {
        val p = Properties()
        p["mail.smtp.host"] = mailServerHost
        p["mail.smtp.port"] = mailServerPort
        p["mail.smtp.auth"] = if (validate) "true" else "false"
        return p
    }
}