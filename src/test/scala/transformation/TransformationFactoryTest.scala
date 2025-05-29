package me.betanow
package transformation

import source.FileSourceFactory

import org.scalatest.funsuite.AnyFunSuite

class TransformationFactoryTest extends AnyFunSuite {
  test("Fields") {
    val fields = new FieldsTransformation()

    val sourceFactory = FileSourceFactory()
    val data = sourceFactory("src/test/resources/transformation/data.csv").getOrElse(null)
    var expectedData = sourceFactory("src/test/resources/transformation/data_fielded1.csv").getOrElse(null)

    assert(data.isInstanceOf[models.Data])
    assert(expectedData.isInstanceOf[models.Data])

    // Rename, drop, and add fields
    var fieldsRequest = fields
      .renameFields("name", "firstName")
      .dropFields("city")
      .addField("country", "USA")

    var result = fieldsRequest.transform(data).getOrElse(null)
    assert(expectedData.content === result.content)

    expectedData = sourceFactory("src/test/resources/transformation/data_fielded2.csv").getOrElse(null)
    assert(expectedData.isInstanceOf[models.Data])

    // Try selecting before renaming fields
    fieldsRequest = fields
      .selectFields("firstName", "age", "city")
      .renameFields("name", "firstName")

    result = fieldsRequest.transform(data).getOrElse(null)
    assert(expectedData.content === result.content)
  }

  test("Filter") {
    val filter = new FilterTransformation()

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
