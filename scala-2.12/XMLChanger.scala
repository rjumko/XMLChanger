/**
  * Created by Администратор on 14.12.15.
  */

import scala.xml._
import scala.collection.immutable._
import com.typesafe.config._
import java.io.File

object XMLChanger {

  private val conf =  ConfigFactory.parseFile(new File("./application.conf"))
  private val inputFolder = conf.getString("XML.inputFolder")
  private val outputFolder = conf.getString("XML.outputFolder")
  private val storedFolder = conf.getString("XML.storedFolder")
  private val format = "80020"
  private val inn = "7722245108"
  private val innTEU = "4200003300"
  private val innKuzbassEnergosbit = "4200003302"
  private val innFSKEESMES = "4200003301"

  def converter(XMLfile: Elem): (Elem, Elem) = {
    val validation = (XMLfile \\ "message" \\ "value").length
    if (validation != 4224) Unit
    val messageNumber = getMessageNumber(XMLfile)
    val psFSKEESMES =
      List( "422070134107101" -> "ПС \"Распадская-3\", 110/35/6 кВ, ОРУ-110 кВ, ввод Т-1",
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
      List( "422070136314101" -> "ПС Распадская-1, 110/6 кВ, РУ-6 кВ, ф.яч.11",
            "422070139208101" -> "ПС \"Томусинская\" 110/35/6 кВ, ОРУ-35 кВ, ВЛ-35 кВ У-16",
            "422070139208201" -> "ПС \"Томусинская\" 110/35/6 кВ, ОРУ-35 кВ, ВЛ-35 кВ У-15",
            "422080086108101" -> "ПС Клетьевая, 35/6 кВ, ОРУ-35 кВ, ввод Т-1",
            "422080086108201" -> "ПС Клетьевая, 35/6 кВ, ОРУ-35 кВ, ввод Т-2",
            "422080086214101" -> "ПС Клетьевая, 35/6 кВ, РУ-6 кВ, ф.яч.17")
    def newPSNode(ps:(String, String)):Seq[Node] = {
      val result1 = XMLfile \\ "message" \ "area" \ "measuringpoint"
      <measuringpoint code={ps._1} name={ps._2}>
        {result1.filter(e => (e \ "@code").text == ps._1 ) \ "measuringchannel"}
      </measuringpoint>
    }
    val xmlOut: Elem =
      <message class="80020" number={messageNumber} version="2">
        {XMLfile \\ "datetime"}
        <sender>
          <inn>{inn}</inn>
          <name>ООО {scala.xml.Unparsed(""""Мечел-Энерго"""")}</name>
        </sender>
        <area>
          <inn>{innFSKEESMES}</inn>
          <name>МЕЧЕЛ-ЭНЕРГО (Томусинское энергоуправление)-ФСК ЕЭС МЭС Сибири (сети Кемеровской обл.)</name>
          {for (a <- psFSKEESMES) yield newPSNode(a)}
        </area>
        <area>
          <inn>{innKuzbassEnergosbit}</inn>
          <name>МЕЧЕЛ-ЭНЕРГО (Томусинское энергоуправление)-Кузбассэнергосбыт</name>
          {for (a <- psKuzbassEnergosbit) yield newPSNode(a)}
        </area>
      </message>

    val xmlFSKEESMES: Elem =
      <message class="80020" number={messageNumber} version="2">
        {XMLfile \\ "datetime"}
        <sender>
          <inn>{inn}</inn>
          <name>ООО {scala.xml.Unparsed(""""Мечел-Энерго"""")}</name>
        </sender>
        <area>
          <inn>{innFSKEESMES}</inn>
          <name>МЕЧЕЛ-ЭНЕРГО (Томусинское энергоуправление)-ФСК ЕЭС МЭС Сибири (сети Кемеровской обл.)</name>
          {for (a <- psFSKEESMES) yield newPSNode(a)}
        </area>
      </message>

    val xmlKuzbassEnergosbit: Elem =
      <message class="80020" number={messageNumber} version="2">
        {XMLfile \\ "datetime"}
        <sender>
          <inn>{inn}</inn>
          <name>ООО {scala.xml.Unparsed(""""Мечел-Энерго"""")}</name>
        </sender>
        <area>
          <inn>{innKuzbassEnergosbit}</inn>
          <name>МЕЧЕЛ-ЭНЕРГО (Томусинское энергоуправление)-Кузбассэнергосбыт</name>
          {for (a <- psKuzbassEnergosbit) yield newPSNode(a)}
        </area>
      </message>

    def magicXML(xml: Elem): Elem = {
      val p = new scala.xml.PrettyPrinter(100, 0)                                 // были проблемы
      XML.loadString(XML.loadString(p.format(xml)).toString)   // с отображением, эта магия исправила
    }

    (magicXML(xmlFSKEESMES), magicXML(xmlKuzbassEnergosbit))
  }

  def getDateXML(xml: Elem): String = {
    (xml \\ "message" \ "datetime" \ "day").text
  }
  def getMessageNumber(xml: Elem): String = {
    ((xml \\ "message" \ "@number").text.toInt+10000).toString
  }

  def convert() {
    val listOfFiles = Utils.getListFiles(inputFolder)
    if (!listOfFiles.isEmpty) {
      listOfFiles.foreach({ i =>
        val xmlSave = converter(XML.loadString(XML.loadFile(i).toString()))
        if (xmlSave._1.nonEmpty) {
          XML.save(outputFolder + format + "_" + inn + "_" +
            getDateXML(xmlSave._1) + "_" + getMessageNumber(xmlSave._1) + "_" + innFSKEESMES + ".xml", xmlSave._1, "windows-1251", xmlDecl = true, null)
        }
        if (xmlSave._2.nonEmpty) {
          XML.save(outputFolder + format + "_" + inn + "_" +
            getDateXML(xmlSave._2) + "_" + getMessageNumber(xmlSave._2) + "_" + innKuzbassEnergosbit + ".xml", xmlSave._2, "windows-1251", xmlDecl = true, null)
        }
       // Utils.mv(i, storedFolder + i.split(File.separatorChar).last)
      })
    }
  }
}
