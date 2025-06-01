package me.betanow
package sink

import models.Data

import org.scalatest.funsuite.AnyFunSuite

import java.io.File

class SinkFactoryTest extends AnyFunSuite {

  // Create test data for each test
  def createTestData(path: String): Data = Data(
    path,
    List(
      Map("name" -> "John", "age" -> 30, "city" -> "New York", "email" -> "john@exemple.com"),
      Map("name" -> "Jane", "age" -> 25, "city" -> "Los Angeles", "email" -> "jane@exemple.com"),
      Map("name" -> "Doe", "age" -> 35, "city" -> "Chicago", "email" -> "doe@exemple.com")
    )
  )

  // Helper method to clean up test files
  def deleteFile(path: String): Unit = {
    val file = new File(path)
    if (file.exists()) {
      file.delete()
    }
  }

  test("Test for JSON file sink") {
    val path = "src/test/resources/sink/test_output.json"
    deleteFile(path) // Clean up before test

    val factory = new SinkFactory()
    val result = factory(path, createTestData(path))
    assert(result.isRight)

    // Verify file exists
    val file = new File(path)
    assert(file.exists())

    // Clean up after a test
    deleteFile(path)
  }

  test("Test for CSV file sink") {
    val path = "src/test/resources/sink/test_output.csv"
    deleteFile(path) // Clean up before test

    val factory = new SinkFactory()
    val result = factory(path, createTestData(path))
    assert(result.isRight)

    // Verify file exists
    val file = new File(path)
    assert(file.exists())

    // Clean up after a test
    deleteFile(path)
  }

  test("Test for XML file sink") {
    val path = "src/test/resources/sink/test_output.xml"
    deleteFile(path) // Clean up before test

    val factory = new SinkFactory()
    val result = factory(path, createTestData(path))
    assert(result.isRight)

    // Verify file exists
    val file = new File(path)
    assert(file.exists())

    // Clean up after a test
    deleteFile(path)
  }

  test("Test for unsupported file extension") {
    val path = "src/test/resources/sink/test_output.txt"
    deleteFile(path) // Clean up before test

    val factory = new SinkFactory()
    val result = factory(path, createTestData(path))
    assert(result.isLeft)

    // Verify the file does not exist
    val file = new File(path)
    assert(!file.exists())
  }

  test("Test for round-trip JSON") {
    val path = "src/test/resources/sink/round_trip.json"
    deleteFile(path) // Clean up before test

    // Create test data
    val testData = createTestData(path)

    // Write data
    val sinkFactory = new SinkFactory()
    val writeResult = sinkFactory(path, testData)
    assert(writeResult.isRight)

    // Read data back
    val sourceFactory = new source.SourceFactory()
    val readResult = sourceFactory(path)
    assert(readResult.isRight)

    // Verify data is the same
    readResult.map { data =>
      assert(data.content == testData.content)
    }

    // Clean up after a test
    deleteFile(path)
  }
}
