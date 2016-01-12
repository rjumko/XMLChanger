import java.io.File

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object Starter extends App {

  val system = ActorSystem("MySystem")
  system.scheduler.schedule(0 seconds, 5 minutes){
	//Utils.getListFiles("\\").foreach(println(_))
    println("start system")
    MailReceiver.checkMail()
    XMLChanger.convert
    MailSender.generateAndSendEmail
  }
}



