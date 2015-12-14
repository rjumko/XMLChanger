import java.io.File
import javax.mail._
import javax.mail.internet._

import com.typesafe.config.ConfigFactory

import scala.util.Try
import scala.xml.XML


object Starter extends App {

  val conf = ConfigFactory.load()

  //System.setProperty("simple-lib.whatever", "This value comes from a system property")

  // Load our own config values from the default location, application.conf
  //val parsedConfig = ConfigFactory.parseFile(new File("src/main/resources/application.conf"))


  def getListFiles(directoryName: String): Array[String] =
    new File(directoryName).listFiles.map(_.getAbsolutePath)

  def mv(oldName: String, newName: String) = {
    Try(new File(oldName).renameTo(new File(newName))).getOrElse(false)}

  //XMLChanger
  generateAndSendEmail()
 /* ProfileTrans.trans("C:\\Users\\Администратор\\Desktop\\rmz — копия\\Профиль №1 0112068115 с 04 12 15 по 10 12 15.txt",
    "C:\\Users\\Администратор\\Desktop\\rmz — копия\\")*/
  def generateAndSendEmail() {
    // Step1
    System.out.println("\n 1st ===> setup Mail Server Properties..")
    val mailServerProperties = System.getProperties()
    //mailServerProperties.put("mail.smtp.port", "587")
    mailServerProperties.put("mail.smtp.auth", "true")
    //mailServerProperties.put("mail.smtp.starttls.enable", "true")
    System.out.println("Mail Server Properties have been setup successfully..")
    // Step2
    System.out.println("\n\n 2nd ===> get Mail Session..")
    val getMailSession = Session.getDefaultInstance(mailServerProperties, null)
    val generateMailMessage = new MimeMessage(getMailSession)
    generateMailMessage.setFrom(new InternetAddress(conf.getString("MailSender.user")))
    generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(conf.getString("MailSender.to").split(';').head))
    generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(conf.getString("MailSender.to").split(';').last))
    generateMailMessage.setSubject("Greetings from Crunchify..")
    val emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin"
    val multipart = new MimeMultipart()
    Starter.getListFiles(conf.getString("XML.outputFolder")).foreach({i =>
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
      transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients())
      transport.close();
    }
    catch
      {
        case e: Exception => println("exception caught: " + e);
      }

  }

}



