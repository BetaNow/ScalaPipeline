package me.betanow
package transformation

import org.scalatest.funsuite.AnyFunSuite

import me.betanow.source.FileSourceFactory

class TransformationFactoryTest extends AnyFunSuite {
  test("Filter") {
    val filter = new Filter()

    val sourceFactory = FileSourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)
    val expectedData = sourceFactory("src/test/resources/transformation/data_filtered.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])
    println(data.content)

    val result = filter
      .filterWhere(content => content("age").asInstanceOf[Int] >= 30)
      .filterNotNull(List("name", "age"))
      .distinct

    assertResult(data, expectedData)
  }
}
