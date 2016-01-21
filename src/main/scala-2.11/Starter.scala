import java.io.File

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object Starter extends App {

  val system = ActorSystem("MySystem")
  val URL = "./logback.xml"
  System.setProperty("logback.configurationFile", URL)
  val logger = Logger(LoggerFactory.getLogger(this.getClass))
  system.scheduler.schedule(0 seconds, 5 minutes){
    logger.info(s"service started")
    MailReceiver.checkMail()
    XMLChanger.convert()
    MailSender.start()
    logger.info(s"service stop")
  }
}



