package me.betanow
package models

/**
 * The Data abstract class represents data.
 *
 * @param filePath The path to the file.
 */
case class Data (filePath: String, content: List[Map[String, Any]]) {
  /**
   * Gets the file name.
   *
   * @return The file name.
   */
  def getFileName: String = {
    val lastSlashIndex: Int = filePath.lastIndexOf('/')
    val lastDotIndex: Int = filePath.lastIndexOf('.')
    filePath.substring(lastSlashIndex + 1, lastDotIndex)
  }

  /**
   * Gets the file extension.
   *
   * @return The file extension.
   */
  def getFileExtension: String = {
    val lastDotIndex: Int = filePath.lastIndexOf('.')
    filePath.substring(lastDotIndex)
  }
}
