/**
  * Created by Администратор on 11.12.15.
  */
object ProfileTrans{

  //trans("C:\\Users\\Администратор\\Desktop\\узунгольская\\Профиль №1 0308071241 с 28 10 15 по 13 12 15.txt", "C:\\Users\\Администратор\\Desktop\\узунгольская\\")

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
      .map(i => i.replaceFirst("(\\d\\d\\.\\d\\d\\.\\d\\d\\t)", "$1 "))
      .map(i => i.replaceAll("000,0000", "0"))
      .map(i => i.replaceAll("000,", "0,"))
      .map(i => i.replaceAll("00([1-9]),", "$1,"))
      .map(i => i.replaceAll("0([1-9][1-9]),", "$1,"))
      .map(i => i.replaceAll("(\\d\\d:\\d\\d)-(\\d\\d:\\d\\d)", "$1 - $2"))
      .zipWithIndex.map(i =>
      if (i._2 % 2 == 0) i._1
      else ((i._1).replaceAll("\\d\\d\\.\\d\\d\\.\\d\\d", "")))
    val b4 = n1.grouped(48).toList
    b4.foreach(i =>
    {
      val date = "\\d\\d\\.\\d\\d\\.\\d\\d".r.findFirstIn(i(0)).get
      val header = List(t1 + date + t1_1 + date, t2 + meterNumber, t3, t4, t5)
      val n = scala.tools.nsc.io.File(outputDir+"Профиль счетчика № "+meterNumber+" за "+date+".txt")
      n.writeAll(header.mkString("\r\n") + "\r\n" + i.mkString("\r\n"))
    })
  }


}
