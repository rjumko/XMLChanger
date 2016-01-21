import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Properties
import javax.mail.Session._
import javax.mail._
import javax.mail.internet.InternetAddress
import com.typesafe.config._
import com.typesafe.scalalogging.Logger
import org.apache.commons.io.{FilenameUtils, IOUtils}
import org.slf4j.LoggerFactory

object MailReceiver
{

  val conf = Utils.conf
  val logger = Logger(LoggerFactory.getLogger(this.getClass))
  val host = conf.getString("MailReceiver.host")
  val port = conf.getInt("MailReceiver.port")
  val username = conf.getString("MailReceiver.user")
  val password = conf.getString("MailReceiver.password")
  val protocol = conf.getString("MailReceiver.protocol")
  val sender = new InternetAddress(conf.getString("MailReceiver.from"))
  val session = getDefaultInstance(new Properties(), null)
  val XMLSign = conf.getString("MailReceiver.XMLSign")
  val inbox = conf.getString("MailReceiver.INBOX")

  def checkMail() {
    logger.info(s"service started")
    try{
      val store = session.getStore(protocol)
      store.connect(host, port, username, password)
      val folder = store.getFolder(inbox)
      folder.open(Folder.READ_WRITE)
      val messages = folder.getMessages
      for (message <- messages) {
        if (message.getFrom.head == sender) {
          logger.info(s"check for attachments")
          if (message.isMimeType("multipart/*"))
            message.getContent match {
              case mp: Multipart =>
                logger.info(s"get attachments")
                val partsCount = mp.getCount
                for (j <- 0 to partsCount - 1) {
                  val part = mp.getBodyPart(j)
                  val disposition = part.getDisposition
                  if ((disposition != null) && ((disposition == "attachment") || (disposition == "inline"))) {
                    val fileName = part.getFileName
                    if (isFileApproaches(fileName)) {
                      saveAttach(fileName, part.getInputStream)
                    }
                  }
                }
              case _ => logger.info(s"receiv mail error: mail has no attachments")
            }
        } else logger.info(s"unsuitable sender $sender")
        message.setFlag(Flags.Flag.DELETED, true)
      }
      folder.close(true)
      store.close()
    }
    catch {
      case e: Exception => logger.error(s"receiv mail error: $e")
    }
  }

  def saveAttach(fileName:String, inputStream:InputStream) {
    try {
      val f = new File(conf.getString("MailReceiver.inboxFolder"), fileName)
      val os = new FileOutputStream(f)
      IOUtils.copy(inputStream, os)
      logger.info(s"save file $fileName OK")
    }
    catch {
      case e: Exception => logger.error(s"save file error: $e")
    }
  }

  def isFileApproaches(fileName:String):Boolean={
    logger.info(s"chek for XML signature")
    fileName.startsWith(XMLSign) &&
      ("xml" == FilenameUtils.getExtension(fileName).toLowerCase())
  }
}
