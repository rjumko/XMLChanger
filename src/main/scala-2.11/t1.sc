import scala.collection.immutable.Seq
import scala.math._
import scala.runtime.RichInt
import BigInt._
import scala.xml._
import util._

val XMLfile = XML.loadFile("C:\\Stored\\80020_7722245108_20151213_33077_4200003300.xml")
val result1 = XMLfile \\ "message" \ "area" \ "measuringpoint"
def newPSNode(ps:(String, String)):Seq[Node] = {
  <measuringpoint code={ps._1} name={ps._2}>
    {result1.filter(e => (e \ "@code").text == ps._1 ) \ "measuringchannel"}
  </measuringpoint>
}
val a = "422070134107101" -> "ПС \"Распадская-3\", 110/35/6 кВ, ОРУ-110 кВ, ввод Т-1"
val xmlOut =
  <message class="80020" number="jk" version="2">
    {XMLfile \\ "datetime"}
    <sender>
      <inn>7722245108</inn>
      <name>ООО {scala.xml.Unparsed(""""Мечел-Энерго"""")}</name>
    </sender>
    <area>
      <inn>4200003301</inn>
      <name>МЕЧЕЛ-ЭНЕРГО (Томусинское энергоуправление)-ФСК ЕЭС МЭС Сибири (сети Кемеровской обл.)</name>
      {newPSNode(a)}
    </area>
  </message>
//result1.filter(e => (e \ "@code").text == a._1 )
