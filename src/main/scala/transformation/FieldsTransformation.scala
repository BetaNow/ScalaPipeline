package me.betanow
package transformation

import models.Data

/**
 * The FieldsTransformation class is a transformation that allows renaming, dropping, and adding fields in the data.
 */
class FieldsTransformation extends TransformationFactory {
  // The fieldRenames map holds the old field names as keys and the new field names as values.
  private var fieldRenames: Map[String, String] = Map()

  // The fieldsToDrop set holds the names of fields that should be dropped from the data.
  private var fieldsToDrop: Set[String] = Set()

  // The fieldsToAdd map holds the names of new fields to be added and their corresponding values.
  private var fieldsToAdd: Map[String, String] = Map()

  // The fieldsToKeep set holds the names of fields that should be kept in the data.
  private var fieldsToKeep: Set[String] = Set()

  /**
   * Resets the transformation to its initial state.
   *
   * @return The FieldsTransformation instance.
   */
  private def reset (): FieldsTransformation = {
    fieldRenames = Map()
    fieldsToDrop = Set()
    fieldsToAdd = Map()
    fieldsToKeep = Set()
    this
  }

  /**
   * Renames a field.
   *
   * @param oldName The old field name.
   * @param newName The new field name.
   * @return The FieldsTransformation instance.
   */
  def renameFields (oldName: String, newName: String): FieldsTransformation = {
    fieldRenames += (oldName -> newName)
    this
  }

  /**
   * Drops fields from the data.
   *
   * @param fieldNames The names of the fields to be dropped.
   * @return The FieldsTransformation instance.
   */
  def dropFields (fieldNames: String*): FieldsTransformation = {
    fieldsToDrop ++= fieldNames.toSet
    this
  }

  /**
   * Selects fields to keep in the data.
   *
   * @param fieldNames The names of the fields to keep.
   * @return The FieldsTransformation instance.
   */
  def selectFields (fieldNames: String*): FieldsTransformation = {
    // Store the fields to keep
    fieldsToKeep = fieldNames.toSet
    // If we're selecting a field that's going to be renamed, we need to include the original field name
    val originalFieldNames = fieldRenames.filter { case (_, newName) => fieldsToKeep.contains(newName) }.keys.toSet
    fieldsToKeep ++= originalFieldNames
    this
  }

  /**
   * Adds a new field with a specified value.
   *
   * @param fieldName The name of the new field.
   * @param value The value of the new field.
   * @return The FieldsTransformation instance.
   */
  def addField (fieldName: String, value: String): FieldsTransformation = {
    fieldsToAdd += (fieldName -> value)
    this
  }

  /**
   * Executes the transformation.
   *
   * @param input The data to be transformed.
   * @return Either a Throwable in case of an error or the transformed data.
   */
  override protected def executeTransform (input: Data): Either[Throwable, Data] = {
    try {
      val fieldedData = input.content.map { row =>
        // Rename fields
        val renamedRow = row.map {
          case (key, value) if fieldRenames.contains(key) => fieldRenames(key) -> value
          case other => other
        }

        // Drop fields
        val droppedRow = renamedRow.filterNot { case (key, _) => fieldsToDrop.contains(key) }

        // Add new fields
        val addedRow = droppedRow ++ fieldsToAdd

        // Apply field selection if fieldsToKeep is not empty
        val selectedRow = if (fieldsToKeep.nonEmpty) {
          // Keep only the fields in fieldsToKeep
          addedRow.filter { case (key, _) => fieldsToKeep.contains(key) }
        } else {
          addedRow
        }

        selectedRow
      }

      val result = Right(input.copy(content = fieldedData))

      result
    } catch {
      case e: Throwable => Left(e)
    } finally {
      reset() // Ensure the transformation state is reset after processing
    }
  }
}
