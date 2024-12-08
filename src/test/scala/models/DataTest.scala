package me.betanow
package models

import org.scalatest.funsuite.AnyFunSuite

class DataTest extends AnyFunSuite {

  test ("Data.getFileName") {
    val data = JsonData("hello/world.file", null) // JsonData is a subclass of Data
    assertResult("world", data.getFileName)
  }

  test("Data.getFileExtension") {
    val data = JsonData("hello/world.file", null) // JsonData is a subclass of Data
    assertResult("file", data.getFileExtension)
  }

}
