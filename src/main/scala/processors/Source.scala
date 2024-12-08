package me.betanow
package processors

import models.Data

/**
 * The Source trait is a generic interface for reading data from a source.
 */
trait Source {
  /**
   * Reads data from a source.
   * 
   * @param path The path to the source.
   * @return Either a Throwable in case of an error or the data read.
   */
  def read (path: String): Either[Throwable, Data]
}
