package me.betanow
package sink

import models.Data

/**
 * The SinkFactory class is a factory for creating sinks based on file extensions.
 */
class SinkFactory {
  /**
   * Creates a sink based on the file extension.
   *
   * @param path The path to the destination.
   * @param data The data to write.
   * @return Either a Throwable in case of an error or a success message.
   */
  def apply (path: String, data: Data): Either[Throwable, String] = {
    val dotIndex: Int = path.lastIndexOf('.')
    val extension: String = if dotIndex != -1 then path.substring(dotIndex + 1) else ""

    extension match
      case "json" => new JsonSink().write(data, path)
      case "csv" => new CsvSink().write(data, path)
      case "xml" => new XmlSink().write(data, path)
      case _ => Left(new Exception(s"Unsupported file extension: $extension"))
  }
}
