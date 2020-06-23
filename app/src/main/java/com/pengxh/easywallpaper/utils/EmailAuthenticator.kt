package com.pengxh.easywallpaper.utils

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/23 22:52
 */
class EmailAuthenticator(name: String?, key: String?) : Authenticator() {
    private var userName = name
    private var password = key

    override fun getPasswordAuthentication(): PasswordAuthentication? {
        return PasswordAuthentication(userName, password)
    }
}