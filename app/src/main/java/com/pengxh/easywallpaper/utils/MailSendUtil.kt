package com.pengxh.easywallpaper.utils

import android.util.Log
import androidx.annotation.NonNull
import com.pengxh.easywallpaper.bean.EmailBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
 * @date: 2020/6/23 23:01
 */
class MailSendUtil {
    companion object {
        private const val Tag: String = "MailSendUtil"

        fun sendTextEmail(emailMessage: String, listener: EmailStatusListener) {
            Log.d(Tag, "sendTextEmail: ")
            GlobalScope.launch(Dispatchers.Main) {
                val result = withContext(Dispatchers.IO) {
                    packageMailBody(createNormalMail(emailMessage))
                }
                listener.onEmailSend(result)
            }
        }

        /**
         * 将邮件打包
         *
         * @param mailBean 待发送的邮件的信息
         */
        private fun packageMailBody(mailBean: EmailBean): Boolean { // 判断是否需要身份认证
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
                val from: Address = InternetAddress(mailBean.fromAddress)
                // 设置邮件消息的发送者
                mailMessage.setFrom(from)
                // 创建邮件的接收者地址，并设置到邮件消息中
                val to: Address = InternetAddress(mailBean.toAddress)
                mailMessage.setRecipient(Message.RecipientType.TO, to)
                // 设置邮件消息的主题
                mailMessage.subject = mailBean.subject
                // 设置邮件消息发送的时间
                mailMessage.sentDate = Date()
                // 设置邮件消息的主要内容
                mailMessage.setText(mailBean.content)
                // 发送邮件
                Transport.send(mailMessage)
                return true
            } catch (ex: MessagingException) {
                ex.printStackTrace()
            }
            return false
        }

        fun sendAttachFileEmail(emailMessage: String, filePath: String, listener: EmailStatusListener) {
            Log.d(Tag, "sendAttachFileEmail: ")
            val file = File(filePath)
            GlobalScope.launch(Dispatchers.Main) {
                val result = withContext(Dispatchers.IO) {
                    packageMailBody(createAttachMail(emailMessage, file), true)
                }
                listener.onEmailSend(result)
            }
        }

        // 发送带附件的邮件
        private fun packageMailBody(mailBean: EmailBean, isAttach: Boolean): Boolean {
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
                val fromAddress: Address = InternetAddress(mailBean.fromAddress)
                // 设置邮件消息的发送者
                mailMessage.setFrom(fromAddress)
                // 创建邮件接收者的地址
                val toAddress: Address = InternetAddress(mailBean.toAddress)
                // 设置邮件消息的接收者
                mailMessage.setRecipient(Message.RecipientType.TO, toAddress)
                // 设置邮件消息的主题
                mailMessage.subject = mailBean.subject
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

        @NonNull
        fun createNormalMail(emailMessage: String): EmailBean {
            val mailBean = EmailBean()
            mailBean.mailServerHost = "smtp.qq.com" //发送方邮箱服务器
            mailBean.mailServerPort = "587" //发送方邮箱端口号
            mailBean.validate = true
            mailBean.userName = "290677893@qq.com" // 发送者邮箱地址
            mailBean.password = "gqvwykjvpnvfbjid" //邮箱授权码，不是密码
            mailBean.toAddress = "2570806855@qq.com" // 接收者邮箱
            mailBean.fromAddress = "290677893@qq.com" // 发送者邮箱
            mailBean.subject = "APP问题反馈" // 邮件主题
            mailBean.content = emailMessage // 邮件文本
            return mailBean
        }

        @NonNull
        fun createAttachMail(emailMessage: String, file: File): EmailBean {
            Log.d(Tag, "createAttachMail: 创建邮件实体开始")
            val mailBean = EmailBean()
            mailBean.mailServerHost = "smtp.qq.com" //发送方邮箱服务器
            mailBean.mailServerPort = "587" //发送方邮箱端口号
            mailBean.validate = true
            mailBean.userName = "290677893@qq.com" // 发送者邮箱地址
            mailBean.password = "gqvwykjvpnvfbjid" //邮箱授权码，不是密码
            mailBean.toAddress = "2570806855@qq.com" // 接收者邮箱
            mailBean.fromAddress = "290677893@qq.com" // 发送者邮箱
            mailBean.subject = "APP问题反馈" // 邮件主题
            mailBean.content = emailMessage
            mailBean.attachFile = file
            Log.d(Tag, "createAttachMail: 创建邮件实体结束")
            return mailBean
        }
    }
}