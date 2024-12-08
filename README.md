# ScalaPipeline

A flexible and extensible Data Pipeline Framework built with Scala, designed to streamline data processing workflows.

## Overview

This framework provides a type-safe, functional approach to building data processing pipelines. It allows users to:
- Read data from various sources
- Apply transformations and validations
- Output processed data to different destinations

## Features (Planned)

- Modular pipeline components
- Type-safe transformations
- Error handling and recovery
- Extensible source/sink system
- Monitoring and logging capabilities
- Parallel processing support

## Transformations
### Filter
- filterWhere(condition) - keep records matching condition
- filterNotNull(fields) - remove records with null in specified fields
- distinct - remove duplicate records

### Fields
- renameFields(oldName -> newName) - rename fields
- dropFields(fieldNames) - remove fields
- selectFields(fieldNames) - keep only these fields
- addField(name, value) - add new field with constant value

### Transform
- mapValues(field, function) - transform specific field values
- trim(fields) - remove whitespace
- normalize(field, format) - standardize dates/numbers
- toLowerCase/toUpperCase(fields) - change case of field values

### Aggregate
- groupBy(field) - group records by field
- count(field) - count records
- sum(field) - sum field values

## Tech Stack

- Scala
- SBT

## Project Status

🚧 Under Development  
Currently working on core components and basic functionality.