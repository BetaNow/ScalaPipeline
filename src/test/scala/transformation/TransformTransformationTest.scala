package me.betanow
package transformation

import source.SourceFactory
import models.Data

import org.scalatest.funsuite.AnyFunSuite

class TransformTransformationTest extends AnyFunSuite {
  test("Map field transformation") {
    val transform = new TransformTransformation()

    val sourceFactory = SourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])

    // Test mapping a field with a transformation function
    val transformRequest = transform
      .mapField("email", email => email.replace("exemple", "example"))

    val result = transformRequest.transform(data).getOrElse(null)

    // Verify that emails have been transformed
    result.content.foreach { row =>
      val email = row.getOrElse("email", "").toString
      if (email.nonEmpty) {
        assert(!email.contains("exemple"))
        assert(email.contains("example"))
      }
    }
  }

  test("Trim fields transformation") {
    val transform = new TransformTransformation()

    // Create test data with whitespace
    val testData = Data(
      "test_data.csv",
      List(
        Map("name" -> "  John  ", "age" -> "30", "city" -> " New York "),
        Map("name" -> "Jane  ", "age" -> "25", "city" -> "  Los Angeles")
      )
    )

    // Test trimming fields
    val transformRequest = transform
      .trimFields("name", "city")

    val result = transformRequest.transform(testData).getOrElse(null)

    // Verify that fields have been trimmed
    assert(result.content.head("name") === "John")
    assert(result.content.head("city") === "New York")
    assert(result.content(1)("name") === "Jane")
    assert(result.content(1)("city") === "Los Angeles")
  }

  test("Normalize field transformation") {
    val transform = new TransformTransformation()

    val sourceFactory = SourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])

    // Test normalizing a field to a specific value
    val transformRequest = transform
      .normalizeField("city", "Unknown")

    val result = transformRequest.transform(data).getOrElse(null)

    // Verify that city has been normalized to "Unknown"
    result.content.foreach { row =>
      assert(row.getOrElse("city", "") === "Unknown")
    }
  }

  test("To lowercase transformation") {
    val transform = new TransformTransformation()

    val sourceFactory = SourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])

    // Test converting fields to lowercase
    val transformRequest = transform
      .toLowerCase("name", "city")

    val transformResult = transformRequest.transform(data)

    // Check if there was an error
    assert(transformResult.isRight, s"Transform failed with error: ${transformResult.left.getOrElse("Unknown error")}")

    val result = transformResult.getOrElse(null)

    // Verify that fields have been converted to lowercase
    result.content.foreach { row =>
      val name = row.getOrElse("name", "").toString
      val city = row.getOrElse("city", "").toString

      if (name.nonEmpty) {
        assert(name === name.toLowerCase)
      }

      if (city.nonEmpty && city != "null") {
        assert(city === city.toLowerCase)
      }
    }
  }

  test("To uppercase transformation") {
    val transform = new TransformTransformation()

    val sourceFactory = SourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])

    // Test converting fields to uppercase
    val transformRequest = transform
      .toUpperCase("name", "city")

    val result = transformRequest.transform(data).getOrElse(null)

    // Verify that fields have been converted to uppercase
    result.content.foreach { row =>
      val name = row.getOrElse("name", "").toString
      val city = row.getOrElse("city", "").toString

      if (name.nonEmpty) {
        assert(name === name.toUpperCase)
      }

      if (city.nonEmpty && city != "null") {
        assert(city === city.toUpperCase)
      }
    }
  }

  test("Multiple transformations") {
    val transform = new TransformTransformation()

    val sourceFactory = SourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])

    // Test applying multiple transformations
    val transformRequest = transform
      .mapField("email", email => email.replace("exemple", "example"))
      .toUpperCase("name")
      .toLowerCase("city")
      .normalizeField("age", "unknown")

    val result = transformRequest.transform(data).getOrElse(null)

    // Verify that all transformations have been applied
    result.content.foreach { row =>
      val name = row.getOrElse("name", "").toString
      val city = row.getOrElse("city", "").toString
      val age = row.getOrElse("age", "").toString
      val email = row.getOrElse("email", "").toString

      if (name.nonEmpty) {
        assert(name === name.toUpperCase)
      }

      if (city.nonEmpty && city != "null") {
        assert(city === city.toLowerCase)
      }

      assert(age === "unknown")

      if (email.nonEmpty) {
        assert(!email.contains("exemple"))
        assert(email.contains("example"))
      }
    }
  }

  test("Error handling") {
    val transform = new TransformTransformation()

    // Create test data
    val testData = Data(
      "test_error.csv",
      List(
        Map("name" -> "John", "age" -> "30", "city" -> "New York")
      )
    )

    // Test with a transformation function that throws an exception
    val transformRequest = transform
      .mapField("age", age => {
        throw new RuntimeException("Test exception")
        age
      })

    val result = transformRequest.transform(testData)

    // Verify that the error is properly handled
    assert(result.isLeft)
    assert(result.left.getOrElse(null).isInstanceOf[RuntimeException])
    assert(result.left.getOrElse(null).getMessage === "Test exception")
  }
}
