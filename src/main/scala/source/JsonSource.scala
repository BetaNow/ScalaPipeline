package me.betanow
package source

import play.api.libs.json._
import processors.Source
import models.Data

/**
 * The JsonSource class reads data from a JSON file.
 */
class JsonSource extends Source {
  /**
   * Reads data from a JSON file.
   *
   * @param path The path to the JSON file.
   * @return Either a Throwable in case of an error or the data read.
   */
  override def read (path: String): Either[Throwable, Data] ={
    try {
      val file = scala.io.Source.fromFile(path)
      val json: JsValue = Json.parse(file.getLines().mkString)

      // JsValue to List[Map[String, Any]]
      val content: List[Map[String, Any]] = json.as[List[Map[String, Any]]](Reads.list(mapReads))

      // Close the file
      file.close()
      
      Right(Data(path, content))
    } catch {
      case e: Throwable => Left(e)
    }
  }
  
  /**
   * Reads a map from a JSON value.
   * 
   * @return The map read.
   */
  def mapReads: Reads[Map[String, Any]] = {
    case JsObject(fields) =>
      JsSuccess(fields.map { case (key, value) =>
        key -> (value match {
          case JsString(s) => s
          case JsNumber(n) => n
          case JsBoolean(b) => b
          case JsArray(arr) => arr
          case JsObject(obj) => obj
          case JsNull => null
          case _ => throw new IllegalArgumentException("Unsupported JSON value")
        })
      }.toMap)
    case _ => JsError("Expected JsObject")
  }
}
