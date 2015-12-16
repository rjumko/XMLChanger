import java.io.File
import javax.mail._
import javax.mail.internet._

import com.typesafe.config.ConfigFactory

import scala.util.Try
import scala.xml.XML


object Starter extends App {
  XMLChanger
  MailSender.generateAndSendEmail
}



