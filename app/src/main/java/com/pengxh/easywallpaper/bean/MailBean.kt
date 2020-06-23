package com.pengxh.easywallpaper.bean

import java.io.File
import java.util.*

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/23 22:49
 */
class MailBean {
    // 发送邮件的服务器的IP和端口
    var mailServerHost: String? = null
    var mailServerPort: String? = null
    // 登陆邮件发送服务器的用户名和密码
    var userName: String? = null
    var password: String? = null
    // 是否需要身份验证
    var validate = false
    // 邮件的文本内容
    lateinit var content: String
    // 邮件的附件
    lateinit var attachFile: File
    // 邮件附件的文件名
    lateinit var attachFileName: String

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