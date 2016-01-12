/**
  * Created by Администратор on 14.12.15.
  */

import scala.xml._
import scala.collection.immutable._
import com.typesafe.config._

object XMLChanger {

  val conf = ConfigFactory.load

  def converter(XMLfile: Elem): Unit = {
    val messageNumber = ((XMLfile \\ "message" \ "@number").text.toInt+10000).toString
    val format = "80020"
    val inn = "7722245108"
    val innTEU = "4200003300"
    val date = (XMLfile \\ "message" \ "datetime" \ "day").text
    val psFSKEESMES =
      List(  "422070134107101" -> "ПС \"Распадская-3\", 110/35/6 кВ, ОРУ-110 кВ, ввод Т-1",
        "422070134107201" -> "ПС \"Распадская-3\", 110/35/6 кВ, ОРУ-110 кВ, ввод Т-2",
        "422070135107101" -> "ПС \"Распадская-2\", 110/35/6 кВ, ОРУ-110 кВ, ввод Т-1",
        "422070135107201" -> "ПС \"Распадская-2\", 110/35/6 кВ, ОРУ-110 кВ, ввод Т-2",
        "422070136107101" -> "ПС \"Распадская-1\", 110/6 кВ, ОРУ-110 кВ, ввод Т-1",
        "422070136107201" -> "ПС \"Распадская-1\", 110/6 кВ, ОРУ-110 кВ, ввод Т-2",
        "422070137107101" -> "ПС \"Районная котельная\", 110/6 кВ, ОРУ-110 кВ, ввод Т-1",
        "422070137107201" -> "ПС \"Районная котельная\", 110/6 кВ, ОРУ-110 кВ, ввод Т-2",
        "422070138107101" -> "ПС \"Красногорская\", 110/35/10 кВ, ОРУ-110 кВ, ввод Т-1",
        "422070138107201" -> "ПС \"Красногорская\", 110/35/10 кВ, ОРУ-110 кВ, ввод Т-2",
        "422070139107101" -> "ПС \"Томусинская\", 110/35/6 кВ, ОРУ-110 кВ, ввод Т-1",
        "422070139107201" -> "ПС \"Томусинская\", 110/35/6 кВ, ОРУ-110 кВ, ввод Т-2",
        "422140098114101" -> "ЦРП \"РМЗ\", РУ-6 кВ, ввод 2, КЛ-6 кВ, ф.6-16р",
        "422140098114201" -> "ЦРП \"РМЗ\", РУ-6 кВ, ввод 1, КЛ-6 кВ, ф.6-14р",
        "422140099114101" -> "ЦРП \"Томусинского\", РУ-6 кВ, ввод 2, КЛ-6 кВ, ф.6-19п",
        "422140099114201" -> "ЦРП \"Томусинского\", РУ-6 кВ, ввод 1, КЛ-6 кВ, ф.6-17п")
    val psKuzbassEnergosbit =
      List(  "422070136314101" -> "ПС Распадская-1, 110/6 кВ, РУ-6 кВ, ф.яч.11",
        "422070139208101" -> "ПС \"Томусинская\" 110/35/6 кВ, ОРУ-35 кВ, ВЛ-35 кВ У-16",
        "422070139208201" -> "ПС \"Томусинская\" 110/35/6 кВ, ОРУ-35 кВ, ВЛ-35 кВ У-15",
        "422080086108101" -> "ПС Клетьевая, 35/6 кВ, ОРУ-35 кВ, ввод Т-1",
        "422080086108201" -> "ПС Клетьевая, 35/6 кВ, ОРУ-35 кВ, ввод Т-2",
        "422080086214101" -> "ПС Клетьевая, 35/6 кВ, РУ-6 кВ, ф.яч.17")
    val result1 = XMLfile \\ "message" \ "area" \ "measuringpoint"
    def newPSNode(ps:(String, String)):Seq[Node] = {
      <measuringpoint code={ps._1} name={ps._2}>
        {result1.filter(e => (e \ "@code").text == ps._1 ) \ "measuringchannel"}
      </measuringpoint>
    }
    val xmlOut =
      <message class="80020" number={messageNumber} version="2">
        {XMLfile \\ "datetime"}
        <sender>
          <inn>7722245108</inn>
          <name>ООО {scala.xml.Unparsed(""""Мечел-Энерго"""")}</name>
        </sender>
        <area>
          <inn>4200003301</inn>
          <name>МЕЧЕЛ-ЭНЕРГО (Томусинское энергоуправление)-ФСК ЕЭС МЭС Сибири (сети Кемеровской обл.)</name>
          {for (a <- psFSKEESMES) yield newPSNode(a)}
        </area>
        <area>
          <inn>4200003302</inn>
          <name>МЕЧЕЛ-ЭНЕРГО (Томусинское энергоуправление)-Кузбассэнергосбыт</name>
          {for (a <- psKuzbassEnergosbit) yield newPSNode(a)}
        </area>
      </message>
    val p = new scala.xml.PrettyPrinter(100, 0)
    val rr1 = XML.loadString(XML.loadString(p.format(xmlOut)).toString)
    XML.save(conf.getString("XML.outputFolder")+format+"_"+inn+"_"+
      date+"_"+messageNumber+"_"+innTEU+".xml", rr1, "windows-1251", xmlDecl = true, null)
  }

  def convert() {
    if (!Utils.getListFiles(conf.getString("XML.inputFolder")).isEmpty) {
      Utils.getListFiles(conf.getString("XML.inputFolder")).foreach({ i =>
        converter(XML.loadString(XML.loadFile(i).toString()))
        Utils.mv(i, conf.getString("XML.storedFolder") + i.split('\\').last)
      })
    }
  }

}
