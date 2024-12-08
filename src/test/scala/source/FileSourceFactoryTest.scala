package me.betanow
package source

import models.JsonData

import org.scalatest.funsuite.AnyFunSuite

class FileSourceFactoryTest extends AnyFunSuite {
  test("Test for JSON file source") {
    val path = "src/test/resources/test.json"
    val factory = new FileSourceFactory()
    val result = factory(path)
    assert(result.isRight)

    result.map { data =>
      assert(data.isInstanceOf[JsonData])
      assert(data.getFileName == "test")
      assert(data.getFileExtension == ".json")
    }

    // Convert to JsonData to access content
    val jsonData = result.getOrElse(null).asInstanceOf[JsonData]
    val expected: List[Map[String, Any]] = List(
      Map("name" -> "John"), Map("age" -> 30), Map("city" -> "New York"), Map("email" -> "john@exemple.com"),
      Map("name" -> "Jane"), Map("age" -> 25), Map("city" -> "Los Angeles"), Map("email" -> "jane@exemple.com"),
      Map("name" -> "Doe"), Map("age" -> 35), Map("city" -> "Chicago"), Map("email" -> "doe@exemple.com")
    )
    assertResult(expected, jsonData.getContent)
  }
}