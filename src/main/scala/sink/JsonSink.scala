package me.betanow
package sink

import models.Data
import processors.Sink

import play.api.libs.json.*

import java.io.{File, PrintWriter}
import scala.util.Using

/**
 * The JsonSink class writes data to a JSON file.
 */
class JsonSink extends Sink {
  /**
   * Implicit `Writes` instance for `Any` type that handles various types.
   * This is used to convert any value to JSON format.
   */
  private val anyWrites: Writes[Any] = Writes {
    case s: String => JsString(s)
    case n: Int => JsNumber(n)
    case n: Long => JsNumber(n)
    case n: Double => JsNumber(n)
    case n: BigDecimal => JsNumber(n)
    case b: Boolean => JsBoolean(b)
    case null => JsNull
    case seq: Seq[?] => JsArray(seq.map(Json.toJson(_)(anyWrites)))
    case m: Map[?, ?] =>
      // Safe cast because we only ever recurse with Map[String, Any]
      val obj = m.asInstanceOf[Map[String, Any]]
      JsObject(obj.view.mapValues(Json.toJson(_)(anyWrites)).toMap)
    case other => JsString(other.toString)
  }

  /**
   * Writes data to a JSON file.
   *
   * @param data The data to write.
   * @param path Destination file.
   */
  override def write (data: Data, path: String): Either[Throwable, String] = {
    Using(new PrintWriter(new File(path))) { writer =>
      val json = Json.toJson(data.content) // implicit mapWrites is in scope
      writer.write(Json.prettyPrint(json))
    }
      // At this point we have a Try[Unit]; map it to the success message
      .map(_ => s"Data successfully written to $path")
      // and finally convert the Try to Either
      .toEither
  }

  /**
   * Implicit `Writes` instance for `Map[String, Any]` type.
   * This is used to convert a map to JSON format.
   */
  private implicit val mapWrites: Writes[Map[String, Any]] = Writes { m =>
    JsObject(m.view.mapValues(Json.toJson(_)(anyWrites)).toMap)
  }
}