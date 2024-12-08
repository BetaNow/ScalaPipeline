package me.betanow
package source

import models.Data
import source.JsonSource

/**
 * The FileSourceFactory class is a factory for creating sources based on file extensions.
 */
class FileSourceFactory {
  /**
   * Creates a source based on the file extension.
   * 
   * @param path The path to the source.
   * @return Either a Throwable in case of an error or the data read.
   */
  def apply (path: String): Either[Throwable, Data] = {
    val dotIndex: Int = path.lastIndexOf('.')
    val extension: String = if dotIndex != -1 then path.substring(dotIndex + 1) else ""

    extension match
      case "json" => new JsonSource().read(path)
      case "csv" => new CsvSource().read(path)
      case "xml" => new XmlSource().read(path)
      case _ => Left(new Exception(s"Unsupported file extension: $extension"))
  }
}
