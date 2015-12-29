import java.io.File

import scala.util.Try

/**
  * Created by Администратор on 16.12.15.
  */
object Utils {
  def getListFiles(directoryName: String): Array[String] =
    new File(directoryName).listFiles.map(_.getAbsolutePath)

  def mv(oldName: String, newName: String) = {
    new File(newName).delete()
    Try(new File(oldName).renameTo(new File(newName))).getOrElse(false)}
}
