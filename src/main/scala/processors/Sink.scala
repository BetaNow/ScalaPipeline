package me.betanow
package processors

import models.Data

/**
 * The Sink trait is a generic interface for writing data to a destination.
 */
trait Sink {
  /**
   * Writes data to a destination.
   *
   * @param data The data to write.
   * @param path The path to the destination.
   * @return Either a Throwable in case of an error or a success message.
   */
  def write(data: Data, path: String): Either[Throwable, String]
}