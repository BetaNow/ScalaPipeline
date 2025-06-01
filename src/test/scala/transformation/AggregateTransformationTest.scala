package me.betanow
package transformation

import source.SourceFactory
import models.Data

import org.scalatest.funsuite.AnyFunSuite

class AggregateTransformationTest extends AnyFunSuite {
  test("Aggregate") {
    val aggregate = new AggregateTransformation()

    val sourceFactory = SourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])

    val aggregateRequest = aggregate
      .groupByFields("name", "city")
      .count("email")
      .mean("age")

    val result = aggregateRequest.transform(data).getOrElse(null)

    // Create expected data directly - order doesn't matter for comparison
    val expectedContent = Set(
      Map("name" -> null, "age" -> 40, "city" -> "Los Angeles", "email" -> 1),
      Map("name" -> "Jane", "age" -> 40, "city" -> null, "email" -> 1),
      Map("name" -> "Jane", "age" -> 25, "city" -> "Los Angeles", "email" -> 1),
      Map("name" -> "Doe", "age" -> 35, "city" -> "Chicago", "email" -> 1),
      Map("name" -> "John", "age" -> 30, "city" -> "New York", "email" -> 3)
    )

    // Compare result with expected content as sets to ignore order
    assert(result.content.toSet === expectedContent)
  }

  test("Aggregate with sum") {
    val aggregate = new AggregateTransformation()

    val sourceFactory = SourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])

    val aggregateRequest = aggregate
      .groupByFields("name")
      .sum("age")
      .count("email")

    val result = aggregateRequest.transform(data).getOrElse(null)

    // Create expected data directly - including city field and correct order
    val expectedContent = Set(
      Map("name" -> "Jane", "age" -> 65, "city" -> "Los Angeles", "email" -> 2),
      Map("name" -> "Doe", "age" -> 35, "city" -> "Chicago", "email" -> 1),
      Map("name" -> "John", "age" -> 90, "city" -> "New York", "email" -> 3),
      Map("name" -> null, "age" -> 40, "city" -> "Los Angeles", "email" -> 1)
    )

    // Compare result with expected content as sets to ignore order
    assert(result.content.toSet === expectedContent)
  }

  test("Aggregate with no groupBy fields") {
    val aggregate = new AggregateTransformation()

    val sourceFactory = SourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])

    val aggregateRequest = aggregate
      .count("email")
      .mean("age")

    // Should return an error because no groupBy fields are specified
    val result = aggregateRequest.transform(data)

    assert(result.isLeft)
    assert(result.left.isInstanceOf[IllegalArgumentException])
    assert(result.left.getOrElse(null).getMessage === "Group by must be set before transformation.")
  }

  test("Aggregate with no aggregation functions") {
    val aggregate = new AggregateTransformation()

    val sourceFactory = SourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])

    val aggregateRequest = aggregate
      .groupByFields("name", "city")

    // Should return an error because no aggregation functions are specified
    val result = aggregateRequest.transform(data)

    assert(result.isLeft)
    assert(result.left.isInstanceOf[IllegalArgumentException])
    assert(result.left.getOrElse(null).getMessage === "At least one aggregation function must be set before transformation.")
  }
}
