package me.betanow
package source

import models.Data

import org.scalatest.funsuite.AnyFunSuite

class FileSourceFactoryTest extends AnyFunSuite {

  val expectedContent: List[Map[String, Any]] = List(
    Map("name" -> "John"), Map("age" -> 30), Map("city" -> "New York"), Map("email" -> "john@exemple.com"),
    Map("name" -> "Jane"), Map("age" -> 25), Map("city" -> "Los Angeles"), Map("email" -> "jane@exemple.com"),
    Map("name" -> "Doe"), Map("age" -> 35), Map("city" -> "Chicago"), Map("email" -> "doe@exemple.com")
  )

  test("Test for JSON file source") {
    val path = "src/test/resources/test.json"
    val factory = new FileSourceFactory()
    val result = factory(path)
    assert(result.isRight)

    result.map { data =>
      assert(data.isInstanceOf[Data])
      assert(data.getFileName == "test")
      assert(data.getFileExtension == ".json")
    }

    // Convert to JsonData to access content
    val jsonData = result.getOrElse(null)
    assertResult(expectedContent, jsonData.content)
  }

  test("Test for CSV file source") {
    val path = "src/test/resources/test.csv"
    val factory = new FileSourceFactory()
    val result = factory(path)
    assert(result.isRight)

    result.map { data =>
      assert(data.isInstanceOf[Data])
      assert(data.getFileName == "test")
      assert(data.getFileExtension == ".csv")
    }

    // Convert to JsonData to access content
    val csvData = result.getOrElse(null)
    assertResult(expectedContent, csvData.content)
  }

  test("Test for XML file source") {
    val path = "src/test/resources/test.xml"
    val factory = new FileSourceFactory()
    val result = factory(path)
    assert(result.isRight)

    result.map { data =>
      assert(data.isInstanceOf[Data])
      assert(data.getFileName == "test")
      assert(data.getFileExtension == ".xml")
    }

    // Convert to JsonData to access content
    val xmlData = result.getOrElse(null)
    assertResult(expectedContent, xmlData.content)
  }

  test("Test for unsupported file extension") {
    val path = "src/test/resources/test.txt"
    val factory = new FileSourceFactory()
    val result = factory(path)
    assert(result.isLeft)
  }

}