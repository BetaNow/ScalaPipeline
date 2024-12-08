package me.betanow
package source

import models.Data
import processors.Source

/**
 * The CsvSource class reads data from a CSV file.
 */
class CsvSource extends Source {
  /**
   * Reads data from a CSV file.
   *
   * @param path The path to the CSV file.
   * @return Either a Throwable in case of an error or the data read.
   */
  override def read (path: String): Either[Throwable, Data] ={
    try {
      val file = scala.io.Source.fromFile(path)
      // Get the keys from the first line
      val keys = file.getLines().next().split(",").map(_.trim)
      // Get the content
      val content = file.getLines().map { line =>
        keys.zip(line.split(",").map(_.trim)).toMap
      }.toList

      // Close the file
      file.close()
      
      Right(Data(path, convertTypes(content)))
    } catch {
      case e: Throwable => Left(e)
    }
  }

  /**
   * Converts the data types to the correct types.
   *
   * @param data The data to convert.
   * @return The data with the correct types.
   */
  private def convertTypes (data: List[Map[String, Any]]): List[Map[String, Any]] = {
    def convertValue (value: Any): Any = value match {
      case s: String =>
        // Try to convert to Int
        try {
          s.toInt
        } catch {
          case _: NumberFormatException =>
            // Try to convert to Double
            try {
              s.toDouble
            } catch {
              case _: NumberFormatException =>
                // Try to convert to Boolean
                s.toLowerCase match {
                  case "true" | "false" => s.toBoolean
                  case _ => s
                }
            }
          case other => other
        }
    }

    data.map(_.map { case (key, value) => key -> convertValue(value) })
  }
}
