package me.betanow
package processors

import models.Data

/**
 * The Transformation trait is a generic interface for transforming data.
 */
trait Transformation {
  /**
   * Transforms data.
   *
   * @param input The data to be transformed.
   * @return Either a Throwable in case of an error or the transformed data.
   */
  def transform (input: Data): Either[Throwable, Data]
}
