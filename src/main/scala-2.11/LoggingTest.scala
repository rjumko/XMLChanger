/**
  * Created by Администратор on 15.01.16.
  */

import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory




object LoggingTest extends App{

  val logger = Logger(LoggerFactory.getLogger(this.getClass))
  println(System.getProperties)
  logger.info(s"service started")

}
