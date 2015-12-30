/**
  * Created by Администратор on 29.12.15.
  */

import javax.activation.{CommandMap, MailcapCommandMap}
import javax.mail.{Message, Session}
import javax.mail.internet.{MimeBodyPart, MimeMultipart, InternetAddress, MimeMessage}

import com.typesafe.config.ConfigFactory

/**
  * Created by Администратор on 16.12.15.
  */
object MailSender2 {
  def generateAndSendEmail: Unit = {
    val conf = ConfigFactory.load()

    //val mc = CommandMap.getDefaultCommandMap().asInstanceOf[MailcapCommandMap]
    val mc:MailcapCommandMap = CommandMap.getDefaultCommandMap match {
      case x:MailcapCommandMap => x
      case _ => throw new RuntimeException("Unsupported type: MailcapCommandMap")
    }
    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html")
    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml")
    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain")
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed")
    mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822")

    if (Utils.getListFiles(conf.getString("XML.outputFolder")).isEmpty) return
    println("\n 1st ===> setup Mail Server Properties..")
    val mailServerProperties = System.getProperties
    mailServerProperties.put("mail.smtp.auth", "true")
    println("Mail Server Properties have been setup successfully..")
    println("\n\n 2nd ===> get Mail Session..")
    val getMailSession = Session.getDefaultInstance(mailServerProperties, null)
    val generateMailMessage = new MimeMessage(getMailSession)
    generateMailMessage.setFrom(new InternetAddress(conf.getString("MailSender.user")))
    generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(conf.getString("MailSender.to").split(';').head))
    generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(conf.getString("MailSender.to").split(';').last))
    generateMailMessage.setSubject("80020")
    val multipart = new MimeMultipart()
    Utils.getListFiles(conf.getString("XML.outputFolder")).foreach({i =>
      val attachment = new MimeBodyPart()
      attachment.attachFile(i)
      multipart.addBodyPart(attachment)
    })
    generateMailMessage.setContent(multipart)
    System.out.println("Mail Session has been created successfully..")
    // Step3
    System.out.println("\n\n 3rd ===> Get Session and Send mail")
    try {
      val transport = getMailSession.getTransport("smtp")
      transport.connect(conf.getString("MailSender.host"), conf.getString("MailSender.user"), conf.getString("MailSender.password"))
      transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients)
      transport.close();
    }
    catch {
      case e: Exception => println("exception caught: " + e);
    }
    Utils.getListFiles(conf.getString("XML.outputFolder")).foreach({i =>
      Utils.mv(i, conf.getString("XML.storedFolder") + i.split('\\').last)})
  }

}
