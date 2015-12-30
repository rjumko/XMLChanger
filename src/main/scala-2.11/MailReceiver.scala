import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Properties
import javax.mail.Session._
import javax.mail._
import javax.mail.internet.InternetAddress
import com.typesafe.config._
import org.apache.commons.io.{FilenameUtils, IOUtils}

object MailReceiver
{

  println("Start mail check1")
  //checkMail()

  def checkMail() {
    val conf = ConfigFactory.load()
    println("Start mail check2")
    val host = conf.getString("MailReceiver.host")
    val port = conf.getInt("MailReceiver.port")
    val username = conf.getString("MailReceiver.user")
    val password = conf.getString("MailReceiver.password")
    val protocol = conf.getString("MailReceiver.protocol")
    val session = getDefaultInstance(new Properties(), null)

    try{
      val store = session.getStore(protocol)
      store.connect(host, port, username, password)
      val folder = store.getFolder(conf.getString("MailReceiver.INBOX"))
      folder.open(Folder.READ_WRITE)
      val messages = folder.getMessages()
      for (message <- messages) {
        if (message.getFrom.head == new InternetAddress(conf.getString("MailReceiver.from"))) {
          if (message.isMimeType("multipart/*")) {
            println("mail check3")
            //val mp = message.getContent.asInstanceOf[Multipart]
            val mp:Multipart = message.getContent match {
              case x:Multipart => x
              case _ => throw new RuntimeException("Unsupported type: Multipart")
            }
            //
            val partsCount = mp.getCount
            for (j <- 0 to partsCount - 1) {
              val part = mp.getBodyPart(j)
              val disposition = part.getDisposition
              if ((disposition != null) && ((disposition == "attachment") || (disposition== "inline"))) {
                val fileName = part.getFileName
                if (isFileApproaches(fileName)) {
                  saveAttach(fileName, part.getInputStream)
                }
              }
            }
          }
        }
        message.setFlag(Flags.Flag.DELETED, true)
      }
      folder.close(true)
      store.close()
    }
    catch {
      case e: Exception => println("exception caught: " + e)
    }
  }

  def saveAttach(fileName:String, inputStream:InputStream) {
    val conf = ConfigFactory.load()
    try {
      val f = new File(conf.getString("MailReceiver.inboxFolder"), fileName)
      val os = new FileOutputStream(f)
      IOUtils.copy(inputStream, os)
      println("mail check4")
    }
    catch {
      case e: Exception => println("exception caught: " + e)
    }
  }

  def isFileApproaches(fileName:String):Boolean={
    val conf = ConfigFactory.load()
    fileName.startsWith(conf.getString("MailReceiver.XMLSign")) &&
      ("xml" == FilenameUtils.getExtension(fileName).toLowerCase())
  }
}
