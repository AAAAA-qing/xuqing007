package com.fh.shopapi.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import static com.fh.shopapi.utils.SystemConst.ownEmailAccount;

public class SendEmail {

    //发送邮件
    public static void sendEmail(String email, String subJect, String content) {
        Properties prop = new Properties();
        // 设置邮件传输采用的协议smtp
        prop.setProperty("mail.transport.protocol", "smtp");
        // 设置发送人邮件服务器的smtp地址
        // 这里以网易的邮箱smtp服务器地址为例
        prop.setProperty("mail.smtp.host", SystemConst.myEmailSMTPHost);
        // 设置验证机制
        prop.setProperty("mail.smtp.auth", "true");

        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        // 需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        // QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)

        /*final String smtpPort = "465";
        prop.setProperty("mail.smtp.port", smtpPort);
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        prop.setProperty("mail.smtp.socketFactory.port", smtpPort);*/

        // 创建对象回话跟服务器交互
        Session session = Session.getInstance(prop);
        // 会话采用debug模式
        session.setDebug(true);
        // 创建邮件对象
        Message message = null;
        Transport trans = null;
        try {
            message = createSimpleMail(session,email,subJect,content);
            trans = session.getTransport();
            // 链接邮件服务器
            trans.connect(ownEmailAccount, SystemConst.ownEmailPassword);
            // 发送信息
            trans.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭链接
            try {
                trans.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    //创建邮件对象
    private static Message createSimpleMail(Session session,String email, String subJect, String content){
        MimeMessage message = new MimeMessage(session);
        try {
            // 设置发送邮件地址,param1 代表发送地址 param2 代表发送的名称(任意的) param3 代表名称编码方式
            message.setFrom(new InternetAddress(ownEmailAccount, "徐晴", "utf-8"));
            // 代表收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email, "xxx", "utf-8"));
            // To: 增加收件人（可选）
            /*message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("dd@receive.com", "USER_DD", "UTF-8"));
            // Cc: 抄送（可选）
            message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("ee@receive.com", "USER_EE", "UTF-8"));
            // Bcc: 密送（可选）
            message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("ff@receive.com", "USER_FF", "UTF-8"));*/
            // 设置邮件主题
            message.setSubject(subJect);
            // 设置邮件内容
            message.setContent(content, "text/html;charset=utf-8");
            // 设置发送时间
            //message.setSentDate(new Date());
            // 保存上面的编辑内容
            //message.saveChanges();
            // 将上面创建的对象写入本地
            //OutputStream out = new FileOutputStream("MyEmail.eml");
            //message.writeTo(out);
            //out.flush();
            //out.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return message;

    }
}
