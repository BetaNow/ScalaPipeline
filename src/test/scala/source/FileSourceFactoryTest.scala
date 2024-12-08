package me.betanow
package source

import models.Data

import org.scalatest.funsuite.AnyFunSuite

class FileSourceFactoryTest extends AnyFunSuite {

  val expectedContent: List[Map[String, Any]] = List(
    Map("name" -> "John", "age" -> 30, "city" -> "New York", "email" -> "john@exemple.com"),
    Map("name" -> "Jane", "age" -> 25, "city" -> "Los Angeles", "email" -> "jane@exemple.com"),
    Map("name" -> "Doe", "age" -> 35, "city" -> "Chicago", "email" -> "doe@exemple.com")
  )

  test("Test for JSON file source") {
    val path = "src/test/resources/source/test.json"
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
    assert(expectedContent == jsonData.content)
  }

  test("Test for CSV file source") {
    val path = "src/test/resources/source/test.csv"
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
    print (expectedContent)
    assert(expectedContent == csvData.content)
  }

  test("Test for XML file source") {
    val path = "src/test/resources/source/test.xml"
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
    assert(expectedContent == xmlData.content)
  }

  test("Test for unsupported file extension") {
    val path = "src/test/resources/source/test.txt"
    val factory = new FileSourceFactory()
    val result = factory(path)
    assert(result.isLeft)
  }

}