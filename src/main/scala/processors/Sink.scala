package me.betanow
package processors

/**
 * The Sink trait is a generic interface for writing data to a sink.
 *
 * @tparam Data The type of the data to be written.
 */
trait Sink [Data] {
  /**
   * Writes data to a sink.
   *
   * @param input The data to be written.
   * @return Either a Throwable in case of an error or unit.
   */
  def write (input: Data): Either[Throwable, Unit]
}
