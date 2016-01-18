import com.typesafe.scalalogging._
import org.slf4j.LoggerFactory
object Tst {
  val URL = "c:\\logback.xml"
  System.setProperty("logback.configurationFile", URL)
  val logger = Logger(LoggerFactory.getLogger(this.getClass))
  logger.info(s"service started $logger")
}