package me.betanow
package models

/**
 * The JsonData class represents JSON data.
 *
 * @param filePath The path to the file.
 * @param content The content of the file.
 */
class JsonData (filePath: String, content: List[Map[String, Any]]) extends Data(filePath) {
  /**
   * Gets the content of the file.
   *
   * @return The content of the file.
   */
  def getContent: List[Map[String, Any]] = content
}
