package me.betanow
package processors

/**
 * The Source trait is a generic interface for reading data from a source.
 * 
 * @tparam Data The type of the data to be read.
 */
trait Source [Data] {
  /**
   * Reads data from a source.
   * 
   * @param path The path to the source.
   * @return Either a Throwable in case of an error or the data read.
   */
  def read (path: String): Either[Throwable, Data]
}
