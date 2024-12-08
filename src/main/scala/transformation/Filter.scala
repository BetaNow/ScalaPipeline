package me.betanow
package transformation

import models.Data

/**
 * The Filter class is a transformation that filters data based on a set of conditions.
 */
class Filter extends TransformationFactory {
  // The whereCondition field is a list of functions that take a Map[String, Any] and return a Boolean.
  private var whereCondition: List[Map[String, Any] => Boolean] = List()

  // The nullCheckFields field is a list of field names that should not be null.
  private var nullCheckFields: List[String] = List()

  // The isDistinct field is a flag that indicates whether the filtered data should be distinct.
  private var isDistinct: Boolean = false

  /**
   * Adds a filter condition.
   *
   * @param condition The filter condition.
   * @return The Filter instance.
   */
  def filterWhere (condition: Map[String, Any] => Boolean): Filter = {
    // The safeCondition function is a wrapper around the condition function that catches any exceptions and returns false.
    val safeCondition = (content: Map[String, Any]) => {
      try {
        condition(content)
      } catch {
        case _: Throwable => false
      }
    }

    whereCondition = safeCondition :: whereCondition
    this
  }

  /**
   * Adds a list of fields that should not be null.
   *
   * @param fields The list of fields.
   * @return The Filter instance.
   */
  def filterNotNull (fields: List[String]): Filter = {
    nullCheckFields = fields ::: nullCheckFields
    this
  }

  /**
   * Checks if the filtered data should be distinct.
   *
   * @return The Filter instance.
   */
  def distinct: Filter = {
    isDistinct = true
    this
  }

  /**
   * Executes the transformation.
   *
   * @param input The data to be transformed.
   * @return Either a Throwable in case of an error or the transformed data.
   */
  protected def executeTransform (input: Data): Either[Throwable, Data] = {
    try {
      val filteredData = input.content
        .filter(content => whereCondition.forall(_(content)))
        .filter(content => nullCheckFields.forall(field => content.contains(field) && content(field) != null))

      val distinctData = if isDistinct then filteredData.distinct else filteredData

      Right(input.copy(content = distinctData))
    } catch {
      case e: Throwable => Left(e)
    }
  }
}