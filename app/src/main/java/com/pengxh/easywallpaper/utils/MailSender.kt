package com.pengxh.easywallpaper.utils

import android.util.Log
import com.pengxh.easywallpaper.bean.MailBean
import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.*

/**
 * @description: TODO
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @date: 2020/6/23 22:52
 */
class MailSender {
    /**
     * 以文本格式发送邮件
     *
     * @param mailBean 待发送的邮件的信息
     */
    fun sendTextMail(mailBean: MailBean): Boolean { // 判断是否需要身份认证
        var authenticator: EmailAuthenticator? = null
        val pro: Properties = mailBean.getProperties()
        if (mailBean.validate) { // 如果需要身份认证，则创建一个密码验证器
            authenticator = EmailAuthenticator(mailBean.userName, mailBean.password)
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        val sendMailSession =
            Session.getDefaultInstance(pro, authenticator)
        try { // 根据session创建一个邮件消息
            val mailMessage: Message = MimeMessage(sendMailSession)
            // 创建邮件发送者地址
            val from: Address = InternetAddress("2570806855@qq.com")
            // 设置邮件消息的发送者
            mailMessage.setFrom(from)
            // 创建邮件的接收者地址，并设置到邮件消息中
            val to: Address = InternetAddress("290677893@qq.com")
            mailMessage.setRecipient(Message.RecipientType.TO, to)
            // 设置邮件消息的主题
            mailMessage.subject = "APP问题反馈"
            // 设置邮件消息发送的时间
            mailMessage.sentDate = Date()
            // 设置邮件消息的主要内容
            val mailContent: String = mailBean.content
            mailMessage.setText(mailContent)
            // 发送邮件
            Transport.send(mailMessage)
            return true
        } catch (ex: MessagingException) {
            ex.printStackTrace()
        }
        return false
    }

    // 发送带附件的邮件
    fun sendAccessoryMail(mailBean: MailBean): Boolean {
        Log.d("MailSender", "sendAccessoryMail: 发送带附件的邮件")
        // 判断是否需要身份验证
        var authenticator: EmailAuthenticator? = null
        val p: Properties = mailBean.getProperties()
        // 如果需要身份验证，则创建一个密码验证器
        if (mailBean.validate) {
            authenticator = EmailAuthenticator(mailBean.userName, mailBean.password)
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        val sendMailSession =
            Session.getDefaultInstance(p, authenticator)
        try { // 根据session创建一个邮件消息
            val mailMessage: Message = MimeMessage(sendMailSession)
            // 创建邮件发送者的地址
            val fromAddress: Address = InternetAddress("2570806855@qq.com")
            // 设置邮件消息的发送者
            mailMessage.setFrom(fromAddress)
            // 创建邮件接收者的地址
            val toAddress: Address = InternetAddress("290677893@qq.com")
            // 设置邮件消息的接收者
            mailMessage.setRecipient(Message.RecipientType.TO, toAddress)
            // 设置邮件消息的主题
            mailMessage.subject = "APP问题反馈"
            // 设置邮件消息的发送时间
            mailMessage.sentDate = Date()
            // MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象
            val mainPart: Multipart = MimeMultipart()
            val file: File = mailBean.attachFile
            val bodyPart: BodyPart = MimeBodyPart()
            val source: DataSource = FileDataSource(file)
            bodyPart.dataHandler = DataHandler(source)
            bodyPart.fileName = MimeUtility.encodeWord(file.name)
            mainPart.addBodyPart(bodyPart)
            // 将MimeMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart)
            // 发送邮件
            Transport.send(mailMessage)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}