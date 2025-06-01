package me.betanow
package sink

import models.Data
import processors.Sink

import java.io.{File, PrintWriter}

/**
 * The CsvSink class writes data to a CSV file.
 */
class CsvSink extends Sink {
  /**
   * Writes data to a CSV file.
   *
   * @param data The data to write.
   * @param path The path to the CSV file.
   * @return Either a Throwable in case of an error or a success message.
   */
  override def write (data: Data, path: String): Either[Throwable, String] = {
    try {
      // Get all unique keys from all maps
      val allKeys = data.content.flatMap(_.keys).distinct

      // Create a CSV header
      val header = allKeys.mkString(",")

      // Create CSV rows
      val rows = data.content.map { map =>
        allKeys.map { key =>
          map.getOrElse(key, "").toString.replace(",", "\\,")
        }.mkString(",")
      }

      // Write to a file
      val writer = new PrintWriter(new File(path))
      writer.println(header)
      rows.foreach(writer.println)
      writer.close()

      Right(s"Data successfully written to $path")
    } catch {
      case e: Throwable => Left(e)
    }
  }
}