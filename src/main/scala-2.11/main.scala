import javax.mail._
import javax.mail.internet._


object Starter extends App {



  //System.setProperty("simple-lib.whatever", "This value comes from a system property")

  // Load our own config values from the default location, application.conf
  //val parsedConfig = ConfigFactory.parseFile(new File("src/main/resources/application.conf"))


  XMLChanger
  //generateAndSendEmail()
 /* ProfileTrans.trans("C:\\Users\\Администратор\\Desktop\\rmz — копия\\Профиль №1 0112068115 с 04 12 15 по 10 12 15.txt",
    "C:\\Users\\Администратор\\Desktop\\rmz — копия\\")*/
  def generateAndSendEmail() {
    // Step1
    System.out.println("\n 1st ===> setup Mail Server Properties..")
    val mailServerProperties = System.getProperties()
    mailServerProperties.put("mail.smtp.port", "587")
    mailServerProperties.put("mail.smtp.auth", "true")
    mailServerProperties.put("mail.smtp.starttls.enable", "true")
    System.out.println("Mail Server Properties have been setup successfully..")
    // Step2
    System.out.println("\n\n 2nd ===> get Mail Session..")
    val getMailSession = Session.getDefaultInstance(mailServerProperties, null)
    val generateMailMessage = new MimeMessage(getMailSession)
    generateMailMessage.setFrom(new InternetAddress("rjumko@gmail.com"))
    generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("romann631@gmail.com"))
    generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("rjumko@gmail.com"))
    generateMailMessage.setSubject("Greetings from Crunchify..")
    val emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
    val attachment = new MimeBodyPart()
    val multipart = new MimeMultipart()
    attachment.attachFile("C:\\Users\\Администратор\\Documents\\xmlch\\xmlchanger\\80020_7722245108_20151207_32984_4200003300.xml")
    multipart.addBodyPart(attachment)
    generateMailMessage.setContent(multipart)
    System.out.println("Mail Session has been created successfully..")
    // Step3
    System.out.println("\n\n 3rd ===> Get Session and Send mail")
    try {
      val transport = getMailSession.getTransport("smtp")
      transport.connect("smtp.gmail.com", "rjumko@gmail.com", "jhpyP5nFa7Cj")
      transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients())
      transport.close();
    }
    catch
      {
        case e: Exception => println("exception caught: " + e);
      }

  }

}



