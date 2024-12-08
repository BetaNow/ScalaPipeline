package me.betanow
package models

import org.scalatest.funsuite.AnyFunSuite

class DataTest extends AnyFunSuite {

  test ("Data.getFileName") {
    val data = Data("hello/world.file", null)
    assertResult("world", data.getFileName)
  }

  test("Data.getFileExtension") {
    val data = Data("hello/world.file", null)
    assertResult("file", data.getFileExtension)
  }

}
