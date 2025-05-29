package me.betanow
package transformation

import models.Data
import processors.Transformation

/**
 * The AggregateTransformation class is a transformation that aggregates data based on specified fields and aggregation functions.
 */
class AggregateTransformation extends Transformation {
  // The groupBy field holds the names of fields to group by.
  private var groupBy: List[String] = List()
  
  // The aggregations map holds the field names and their corresponding aggregation functions (e.g., "sum", "avg").
  private var aggregations: Map[String, String] = Map()

  /**
   * Resets the transformation to its initial state.
   *
   * @return The AggregateTransformation instance.
   */
  private def reset (): AggregateTransformation = {
    groupBy = List()
    aggregations = Map()
    this
  }

  /**
   * Sets the fields to group by.
   *
   * @param fields The fields to group by.
   * @return The AggregateTransformation instance.
   */
  def groupByFields (fields: String*): AggregateTransformation = {
    groupBy = fields.toList
    this
  }

  /**
   * Add the count aggregation for a specific field.
   * 
   * @param field The field to count.
   * @return The AggregateTransformation instance.
   */
  def count (field: String): AggregateTransformation = {
    aggregations += (field -> "count")
    this
  }

  /**
   * Add the sum aggregation for a specific field.
   * 
   * @param field The field to sum.
   * @return The AggregateTransformation instance.
   */
  def sum (field: String): AggregateTransformation = {
    aggregations += (field -> "sum")
    this
  }

  /**
   * Add the mean aggregation for a specific field.
   * 
   * @param field The field to calculate the mean.
   * @return The AggregateTransformation instance.
   */
  def mean (field: String): AggregateTransformation = {
    aggregations += (field -> "mean")
    this
  }

  /**
   * Transforms data.
   *
   * @param input The data to be transformed.
   * @return Either a Throwable in case of an error or the transformed data.
   */
  override def transform (input: Data): Either[Throwable, Data] = {
    if (groupBy.isEmpty) {
      Left(new IllegalArgumentException("Group by must be set before transformation."))
    } else if (aggregations.isEmpty) {
      Left(new IllegalArgumentException("At least one aggregation function must be set before transformation."))
    } else {
      try {
        val aggregatedData = input.content.groupBy(row => groupBy.map(field => row.getOrElse(field, "")))
          .map { case (key, rows) =>
            val aggregatedRow = rows.head ++ aggregations.map { case (field, func) =>
              func match {
                case "count" => field -> rows.size
                case "sum" => field -> rows.map(_.getOrElse(field, 0).asInstanceOf[Int]).sum
                case "mean" => field -> (rows.map(_.getOrElse(field, 0).asInstanceOf[Int]).sum / rows.size)
                case _ => field -> Left(new IllegalArgumentException(s"Invalid aggregation function: $func"))
              }
            }
            aggregatedRow
          }.toList

        Right(input.copy(content = aggregatedData))
      } catch {
        case e: Throwable => Left(e)
      } finally {
        reset() // Reset the transformation state after processing
      }
    }
  }
}
