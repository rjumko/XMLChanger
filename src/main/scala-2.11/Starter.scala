import akka.actor.ActorSystem
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object Starter extends App {

  val system = ActorSystem("MySystem")
  system.scheduler.schedule(0 seconds, 5 minutes){
    MailReceiver
    XMLChanger.convert
    MailSender.generateAndSendEmail
  }
}



