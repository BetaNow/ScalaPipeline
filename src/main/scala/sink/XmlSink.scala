package me.betanow
package sink

import models.Data
import processors.Sink

import scala.xml.{Elem, Node, XML}

/**
 * The XmlSink class writes data to an XML file.
 */
class XmlSink extends Sink {
  /**
   * Writes data to an XML file.
   *
   * @param data The data to write.
   * @param path The path to the XML file.
   * @return Either a Throwable in case of an error or a success message.
   */
  override def write (data: Data, path: String): Either[Throwable, String] = {
    try {
      // Convert List[Map[String, Any]] to XML
      val xml: Elem = <records>
        {data.content.map(mapToXml)}
      </records>

      // Write to a file
      XML.save(path, xml, "UTF-8", xmlDecl = true, null)

      Right(s"Data successfully written to $path")
    } catch {
      case e: Throwable => Left(e)
    }
  }

  /**
   * Converts a map to an XML element.
   *
   * @param map The map to convert.
   * @return The XML element.
   */
  private def mapToXml (map: Map[String, Any]): Elem = {
    <record>
      {map.map { case (key, value) =>
      <field name={key}>
        {anyToXml(value)}
      </field>
    }}
    </record>
  }

  /**
   * Converts any value to an XML node.
   *
   * @param value The value to convert.
   * @return The XML node.
   */
  private def anyToXml (value: Any): Node = value match {
    case null => <null/>
    case seq: Seq[_] =>
      <array>
        {seq.map(item => <item>
        {anyToXml(item)}
      </item>)}
      </array>
    case map: Map[_, _] =>
      <object>
        {map.asInstanceOf[Map[String, Any]].map { case (k, v) =>
        <entry key={k}>
          {anyToXml(v)}
        </entry>
      }}
      </object>
    case _ => scala.xml.Text(value.toString)
  }
}