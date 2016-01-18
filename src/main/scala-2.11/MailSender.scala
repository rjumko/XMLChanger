import javax.activation.{CommandMap, MailcapCommandMap}
import javax.mail.{Message, Session}
import javax.mail.internet.{MimeBodyPart, MimeMultipart, InternetAddress, MimeMessage}
import java.io.File

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

object MailSender {

  val logger = Logger(LoggerFactory.getLogger(this.getClass))
  val conf = Utils.conf
  val sender = conf.getString("MailSender.user")
  val sendTo = conf.getString("MailSender.to")
  val host = conf.getString("MailSender.host")
  val user = conf.getString("MailSender.user")
  val password = conf.getString("MailSender.password")
  val subject = "80020"

  def start(): Unit = {
    logger.info(s"mail sender start")
    val outputFolder = conf.getString("XML.outputFolder")
    val storedFolder = conf.getString("XML.storedFolder")
    val attachFiles = Utils.getListFiles(outputFolder)
    if (attachFiles.isEmpty) return
    generateAndSendEmail(attachFiles)
    attachFiles.foreach({i =>
      Utils.mv(i, storedFolder + i.split(File.separatorChar).last)})
  }

  def generateAndSendEmail(attachFiles: Array[String]): Unit = {

    val mc:MailcapCommandMap = CommandMap.getDefaultCommandMap match {
      case x:MailcapCommandMap => x
      case _ => throw new RuntimeException("Unsupported type: MailcapCommandMap")
    }
    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html")
    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml")
    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain")
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed")
    mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822")

    logger.info(s"1st ===> setup Mail Server Properties..")
    val mailServerProperties = System.getProperties
    mailServerProperties.put("mail.smtp.auth", "true")
    logger.info(s"mail Server Properties have been setup successfully..")
    logger.info("2nd ===> get Mail Session..")
    val getMailSession = Session.getDefaultInstance(mailServerProperties, null)
    val generateMailMessage = new MimeMessage(getMailSession)
    generateMailMessage.setFrom(new InternetAddress(sender))
    generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo.split(';').head))
    generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(sendTo.split(';').last))
    generateMailMessage.setSubject(subject)
    val multipart = new MimeMultipart()
    attachFiles.foreach({i =>
      logger.info(s"add attach $i")
      val attachment = new MimeBodyPart()
      attachment.attachFile(i)
      multipart.addBodyPart(attachment)
    })
    generateMailMessage.setContent(multipart)
    logger.info(s"mail Session has been created successfully..")
    logger.info(s"3rd ===> Get Session and Send mail")
    try {
      val transport = getMailSession.getTransport("smtp")
      transport.connect(host, user, password)
      transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients)
      transport.close()
      logger.info(s"message sended succesful")
    }
    catch{ case e: Exception => logger.error(s"mail send error: $e")}
  }

}
