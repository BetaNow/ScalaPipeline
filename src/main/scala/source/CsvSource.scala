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
      val lines = file.getLines().toList
      val head = lines.head.split(',')
      val content = List[Map[String, Any]]()

      for (line <- lines.tail) {
        val values = line.split(',')
        val content = head.zip(values).toMap
      }

      // Close the file
      file.close()
      
      Right(Data(path, content))
    } catch {
      case e: Throwable => Left(e)
    }
  }
}
