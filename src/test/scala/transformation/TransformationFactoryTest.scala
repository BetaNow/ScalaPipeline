package me.betanow
package transformation

import source.FileSourceFactory

import org.scalatest.funsuite.AnyFunSuite

class TransformationFactoryTest extends AnyFunSuite {
  test("Filter") {
    val filter = new Filter()

    val sourceFactory = FileSourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)
    val expectedData = sourceFactory("src/test/resources/transformation/data_filtered.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])
    assert(expectedData.isInstanceOf[models.Data])

    val filterRequest = filter
      .filterWhere(content => content("age").asInstanceOf[Int] >= 30)
      .filterNotNull(List("name", "city"))
      .distinct

    val result = filterRequest.transform(data).getOrElse(null)
    assert(expectedData.content === result.content)
  }
}
