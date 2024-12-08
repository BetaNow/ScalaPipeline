package me.betanow
package processors

/**
 * The Transformation trait is a generic interface for transforming data.
 *
 * @tparam Data The type of the data to be transformed.
 */
trait Transformation [Data] {
  /**
   * Transforms data.
   *
   * @param input The data to be transformed.
   * @return Either a Throwable in case of an error or the transformed data.
   */
  def transform (input: Data): Either[Throwable, Data]
}
