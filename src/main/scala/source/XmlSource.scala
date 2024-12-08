package me.betanow
package source

import models.Data
import processors.Source
import scala.xml.XML

/**
 * The XmlSource class reads data from an XML file.
 */
class XmlSource extends Source {
  /**
   * Reads data from an XML file.
   *
   * @param path The path to the XML file.
   * @return Either a Throwable in case of an error or the data read.
   */
  override def read(path: String): Either[Throwable, Data] = {
    try {
      val content = xmlToListMap(XML.loadFile(path).toString)

      content match {
        case Left(e) => Left(e)
        case Right(data) => Right(Data(path, data))
      }
    } catch {
      case e: Throwable => Left(e)
    }
  }

  /**
   * Converts an XML string to a list of maps.
   *
   * @param xmlString The XML string.
   * @return Either a Throwable in case of an error or the list of maps.
   */
  private def xmlToListMap(xmlString: String): Either[Throwable, List[Map[String, String]]] = {
    try {
      val xml = XML.loadString(xmlString)
      val rows = (xml \ "row").map { row =>
        (row \ "column").map { column =>
          (column \ "@name").text -> column.text
        }.toMap
      }.toList
      Right(rows)
    } catch {
      case e: Throwable => Left(e)
    }
  }
}