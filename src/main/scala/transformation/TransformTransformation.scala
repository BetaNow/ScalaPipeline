package me.betanow
package transformation

import models.Data

/**
 * The FieldsTransformation class is a transformation that allows renaming, dropping, and adding fields in the data.
 */
class TransformTransformation extends TransformationFactory {
  // The mapping holds field names as keys and transformation functions as values.
  private var mapping: Map[String, String => String] = Map()

  // The trim set holds field names that should have whitespace trimmed.
  private var trim: Set[String] = Set()

  // The normalize map holds field names as keys and their normalized values as values.
  private var normalize: Map[String, String] = Map()

  // The fieldCases map holds field names as keys and a boolean indicating whether to convert to upper case (true) or lower case (false).
  private var fieldCases: Map[String, Boolean] = Map()

  /**
   * Resets the transformation to its initial state.
   *
   * @return The TransformTransformation instance.
   */
  private def reset (): TransformTransformation = {
    mapping = Map()
    trim = Set()
    normalize = Map()
    fieldCases = Map()
    this
  }

  /**
   * Maps a field to a transformation function.
   *
   * @param field     The field to be transformed.
   * @param transform The transformation function.
   * @return The TransformTransformation instance.
   */
  def mapField (field: String, transform: String => String): TransformTransformation = {
    mapping += (field -> transform)
    this
  }

  /**
   * Trims whitespace from specified fields.
   *
   * @param fields The fields to be trimmed.
   * @return The TransformTransformation instance.
   */
  def trimFields (fields: String*): TransformTransformation = {
    trim ++= fields.toSet
    this
  }

  /**
   * Normalizes fields to a specified value.
   *
   * @param field The field to be normalized.
   * @param value The value to normalize to.
   * @return The TransformTransformation instance.
   */
  def normalizeField (field: String, value: String): TransformTransformation = {
    normalize += (field -> value)
    this
  }

  /**
   * Converts specified fields to lower.
   *
   * @param field The fields to be converted to lower case.
   * @return The TransformTransformation instance.
   */
  def toLowerCase (field: String*): TransformTransformation = {
    field.foreach(f => fieldCases += (f -> false))
    this
  }

  /**
   * Converts specified fields to upper.
   *
   * @param field The fields to be converted to upper case.
   * @return The TransformTransformation instance.
   */
  def toUpperCase (field: String*): TransformTransformation = {
    field.foreach(f => fieldCases += (f -> true))
    this
  }

  /**
   * Executes the transformation.
   *
   * @param input The data to be transformed.
   * @return Either a Throwable in case of an error or the transformed data.
   */
  override protected def executeTransform (input: Data): Either[Throwable, Data] = {
    try {
      var transformedData = input.content.map { row =>
        // Apply field mappings
        val mappedRow = mapping.foldLeft(row) { case (acc, (field, transform)) =>
          val value = acc.getOrElse(field, "")
          val stringValue = if (value == null) "" else value.toString
          acc.updated(field, transform(stringValue))
        }

        // Trim specified fields
        val trimmedRow = trim.foldLeft(mappedRow) { case (acc, field) =>
          val value = acc.getOrElse(field, "")
          val stringValue = if (value == null) "" else value.toString
          acc.updated(field, stringValue.trim)
        }

        // Normalize specified fields
        val normalizedRow = normalize.foldLeft(trimmedRow) { case (acc, (field, value)) =>
          acc.updated(field, value)
        }

        // Convert to lower or upper case as specified
        fieldCases.foldLeft(normalizedRow) { case (acc, (field, toUpper)) =>
          val value = acc.getOrElse(field, "")
          val stringValue = if (value == null) "" else value.toString
          acc.updated(field, if (toUpper) stringValue.toUpperCase else stringValue.toLowerCase)
        }
      }

      Right(input.copy(content = transformedData))
    } catch {
      case e: Throwable => Left(e)
    } finally {
      reset() // Reset the transformation state after execution
    }
  }
}
