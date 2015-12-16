/**
  * Created by Администратор on 15.12.15.
  */

import java.util.concurrent.TimeUnit

import akka._
import akka.actor.Actor
import akka.actor._
import akka.actor.Props
import akka.actor.ActorSystem._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class HelloActor extends Actor {
  def receive = {
    case "hello" => println("hello back at you")
    case _       => println("huh?")
  }
}

object Akka extends App {


  //val system = ActorSystem("HelloSystem")

  val system = ActorSystem("MySystem")
  system.scheduler.schedule(10 seconds, 5 minutes)(println("do something"))
  //system.scheduler.schedule(() => println("Do something"), 0L, 5L, TimeUnit.MINUTES)

  // default Actor constructor
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
  helloActor ! "hello"
  helloActor ! "buenos dias"
}
