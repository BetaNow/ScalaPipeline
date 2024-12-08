package me.betanow
package source

import models.Data
import processors.Source
import scala.xml.XML
import scala.xml.Node

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
  private def xmlToListMap(xmlString: String): Either[Throwable, List[Map[String, Any]]] = {
    try {
      val xml = XML.loadString(xmlString)
      // Get the root element name
      val rootElement = xml.label

      // Get the child element name (e.g., "person")
      val childElements = xml.child.filter(_.isInstanceOf[Node]).map(_.label).distinct
        .filterNot(_.trim.isEmpty)
        .filterNot(_ == "#PCDATA")

      if (childElements.isEmpty) {
        throw new IllegalArgumentException("No valid child elements found in XML")
      }

      val childElementName = childElements.head

      // Extract all child nodes
      val data = (xml \\ childElementName).map { node =>
        // Get all unique field names from the first node
        val fields = node.child.filter(_.isInstanceOf[Node]).filterNot(_.isAtom)

        // Convert each field to a map entry, attempting to convert numbers where possible
        fields.map { field =>
          val key = field.label
          val rawValue = field.text
          val value = rawValue.trim match {
            case n if n.matches("-?\\d+") => n.toLong // For integers
            case n if n.matches("-?\\d*\\.\\d+") => n.toDouble // For decimals
            case s => s // For strings
          }
          key -> value
        }.toMap
      }.toList

      Right(data)
    } catch {
      case e: Throwable => Left(e)
    }
  }
}