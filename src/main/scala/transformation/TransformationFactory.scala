package me.betanow
package transformation

import processors.Transformation
import models.Data

/**
 * The TransformationFactory class is a generic interface for creating transformations.
 */
abstract class TransformationFactory extends Transformation {
  /**
   * Executes the transformation.
   *
   * @param input The data to be transformed.
   * @return Either a Throwable in case of an error or the transformed data.
   */
  protected def executeTransform (input: Data): Either[Throwable, Data]

  /**
   * Transforms data.
   *
   * @param input The data to be transformed.
   * @return Either a Throwable in case of an error or the transformed data.
   */
  override def transform (input: Data): Either[Throwable, Data] = {
    try {
      if (input == null) {
        Left(new IllegalArgumentException("Input data cannot be null"))
      } else {
        executeTransform(input)
      }
    } catch {
      case e: Exception => Left(e)
    }
  }
}
