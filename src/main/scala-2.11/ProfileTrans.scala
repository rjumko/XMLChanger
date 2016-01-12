/**
  * Created by Администратор on 11.12.15.
  */
object ProfileTrans{

  trans("C:\\Users\\Администратор\\Desktop\\узунгольская\\Профиль №1 0307071798 с 28 10 15 по 13 12 15.txt", "C:\\Users\\Администратор\\Desktop\\узунгольская\\")

  def trans(sourceFile: String, outputDir: String): Unit = {
    val t1 = "Профиль с "
    val t1_1 = " по "
    val t2 = "Счетчик № "
    val t3 = "Сохранен 15:01:35"
    val t4 = "Размерность: Активной  кВт, Реактивной  квар"
    val t5 = "Дата	Время	P+	P-	Q+	Q-	Атрибуты"
    val fileLines = io.Source.fromFile(sourceFile, "windows-1251").getLines.toList
    val meterNumber = fileLines(1).split(" ")(2)
    val numPattern = "\\d\\d\\.\\d\\d\\.\\d\\d.*"
    val n1 = fileLines.filter(i => i.matches(numPattern))
      .map(_.replaceFirst("(\\d\\d\\.\\d\\d\\.\\d\\d\\t)", "$1 "))
      .map(_.replaceAll("000,0000", "0"))
      .map(_.replaceAll("000,", "0,"))
      .map(_.replaceAll("00([1-9]),", "$1,"))
      .map(_.replaceAll("0([1-9][1-9]),", "$1,"))
      .map(_.replaceAll("(\\d\\d:\\d\\d)-(\\d\\d:\\d\\d)", "$1 - $2"))
    val b4 = n1.groupBy(_.take(8))
    b4.foreach(i => {
      val dateProfile = i._2.map(
        _.replaceAll("\\d\\d\\.\\d\\d\\.\\d\\d(\\t\\s\\d\\d:30\\s-\\s\\d\\d:00)", "$1")
      ).mkString("\r\n")
      val date = i._1
      val header = List(t1 + date + t1_1 + date, t2 + meterNumber, t3, t4, t5).mkString("\r\n")
      val n = scala.tools.nsc.io.File(outputDir+"Профиль счетчика № "+meterNumber+" за "+date+".txt")
      n.writeAll(header + "\r\n" + dateProfile)
    })
  }


}
