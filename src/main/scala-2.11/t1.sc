import scala.math._
import scala.runtime.RichInt
import BigInt._
import util._

/*val j:BigInt = 123
j.toDouble
(j /% 7)._2
pow(9, 2)
BigInt.probablePrime(10, scala.util.Random).toByteArray

"helonloo".distinct

56.toBinaryString
val y: RichInt = 66
y.toHexString
y.toBinaryString

val t = sqrt(3)
3 - pow(t, 2)
10 max 6
max(66, 87)

BigInt(2)
val y1 = probablePrime(100 ,Random).toString(36)
y1.last
y1(0)
y1 drop 5
//30 /% 7*/










  val fileLines = io.Source.fromFile("C:\\Users\\Администратор\\Desktop\\узунгольская\\Профиль №1 0307071798 с 28 10 15 по 13 12 15.txt", "windows-1251").getLines.toList
  val meterNumber = fileLines(1).split(" ")(2)
  val numPattern = "\\d\\d\\.\\d\\d\\.\\d\\d.*"
  val n1 = fileLines.filter(i => i.matches(numPattern))
    .map(i => i.replaceFirst("(\\d\\d\\.\\d\\d\\.\\d\\d\\t)", "$1 "))
    .map(i => i.replaceAll("000,0000", "0"))
    .map(i => i.replaceAll("000,", "0,"))
    .map(i => i.replaceAll("00([1-9]),", "$1,"))
    .map(i => i.replaceAll("0([1-9][1-9]),", "$1,"))
    .map(i => i.replaceAll("(\\d\\d:\\d\\d)-(\\d\\d:\\d\\d)", "$1 - $2"))
  /*.zipWithIndex.map(i =>
  if (i._2 % 2 == 0) i._1
  else ((i._1).replaceAll("\\d\\d\\.\\d\\d\\.\\d\\d", "")))*/
  val b4 = n1.groupBy(_.take(8))
println(b4)
  //val b5 = n1.grouped(48).toList


