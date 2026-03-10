package avro2s.generator.specific.scala3.record

import avro2s.generator.AstBuilder
import avro2s.generator.AstBuilder.CaseParam
import avro2s.generator.logical.LogicalTypes
import avro2s.generator.logical.LogicalTypes.LogicalTypeConverter
import avro2s.generator.specific.scala3.FieldOps._
import avro2s.generator.{GeneratedCode, GeneratorConfig}
import org.apache.avro.Schema
import org.apache.avro.Schema.Type._

import scala.jdk.CollectionConverters._
import scala.meta._

private[avro2s] class SpecificRecordGenerator(generatorConfig: GeneratorConfig) {
  private val ltc = LogicalTypeConverter(if (generatorConfig.logicalTypesEnabled) LogicalTypes.logicalTypeMap else Map.empty)
  private val getCaseGenerator = new GetCaseGenerator(ltc)
  private val putCaseGenerator = new PutCaseGenerator(ltc)
  private val typeHelpers = new TypeHelpers(ltc)
  import typeHelpers._

  def schemaToScala3Record(schema: Schema, namespace: Option[String]): GeneratedCode = {
    val name = schema.getName
    val fields = schema.getFields.asScala.toList
    val ns = Option(schema.getNamespace).orElse(namespace)

    val imports = List("scala.annotation.switch")
    val classDef = buildClass(name, fields, schema)
    val objectStr = renderCompanionObject(name, fields, schema)

    val code = AstBuilder.renderFileWithRawParts(ns, "/** GENERATED CODE */", imports, List(classDef), List(objectStr))
    GeneratedCode(s"${ns.map(_.replace(".", "/") + "/").getOrElse("") + name}.scala", code)
  }

  private def buildClass(name: String, fields: List[Schema.Field], schema: Schema): Defn.Class = {
    val params = fields.map { field =>
      CaseParam(field.safeName, schemaToScalaType(field.schema, true), Some("var"))
    }

    val body = buildClassBody(name, fields, schema)

    AstBuilder.buildCaseClass(name, params, List("org.apache.avro.specific.SpecificRecordBase"), body)
  }

  private def buildClassBody(name: String, fields: List[Schema.Field], schema: Schema): List[Stat] = {
    val stats = scala.collection.mutable.ListBuffer[Stat]()

    if (fields.nonEmpty) {
      stats += AstBuilder.buildSecondaryConstructor(toThis(fields))
    }

    stats += AstBuilder.parseStat(s"""override def getSchema: org.apache.avro.Schema = $name.SCHEMA$$""")

    if (generatorConfig.logicalTypesEnabled) {
      stats += AstBuilder.parseStat(s"""override def getSpecificData(): org.apache.avro.specific.SpecificData = $name.MODEL$$""")
    }

    stats += buildGetMethod(fields)
    stats += buildPutMethod(fields)

    val hasAnyConversion = fields.exists(f => ltc.getConversionClass(f.schema()).isDefined)
    if (hasAnyConversion) {
      stats += buildGetConversionMethod(name, fields)
    }

    stats.toList
  }

  private def buildGetMethod(fields: List[Schema.Field]): Stat = {
    val cases = fields.zipWithIndex.map { case (field, idx) =>
      getCaseGenerator.generateFieldCase(idx, field)
    } :+ """case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")"""

    val matchBody = cases.mkString("\n")
    AstBuilder.parseStat(
      s"""override def get(field$$: Int): AnyRef = {
         |  (field$$: @switch) match {
         |    $matchBody
         |  }
         |}""".stripMargin
    )
  }

  private def buildPutMethod(fields: List[Schema.Field]): Stat = {
    val cases = fields.zipWithIndex.map { case (field, idx) =>
      putCaseGenerator.generateFieldCase(idx, field)
    } :+ """case _ => throw new org.apache.avro.AvroRuntimeException("Bad index")"""

    val matchBody = cases.mkString("\n")
    AstBuilder.parseStat(
      s"""override def put(field$$: Int, value: Any): Unit = {
         |  (field$$: @switch) match {
         |    $matchBody
         |  }
         |}""".stripMargin
    )
  }

  private def buildGetConversionMethod(name: String, fields: List[Schema.Field]): Stat = {
    val cases = fields.zipWithIndex.map { case (field, idx) =>
      ltc.getConversionClass(field.schema()) match {
        case Some(_) => s"case $idx => $name.${field.safeName}$$Conversion"
        case None => s"case $idx => null"
      }
    } :+ "case _ => null"

    val matchBody = cases.mkString("\n")
    // Scala 3 uses [?] instead of [_] for wildcard type arguments
    AstBuilder.parseStat(
      s"""override def getConversion(field: Int): org.apache.avro.Conversion[_] = {
         |  (field: @switch) match {
         |    $matchBody
         |  }
         |}""".stripMargin
    )
  }

  private def renderCompanionObject(name: String, fields: List[Schema.Field], schema: Schema): String = {
    val stats = scala.collection.mutable.ListBuffer[String]()

    stats += s"""  val SCHEMA$$: org.apache.avro.Schema = new org.apache.avro.Schema.Parser().parse(\"\"\"${schema.toString}\"\"\")"""

    if (generatorConfig.logicalTypesEnabled) {
      val conversions = LogicalTypes.allConversionClasses
      val addConversions = conversions.map(cls => s"    model.addLogicalTypeConversion(new $cls())").mkString("\n")
      stats +=
        s"""  val MODEL$$: org.apache.avro.specific.SpecificData = {
           |    val model = new org.apache.avro.specific.SpecificData()
           |$addConversions
           |    model
           |  }""".stripMargin
    }

    fields.foreach { field =>
      ltc.getConversionClass(field.schema()) match {
        case Some(cls) =>
          // Scala 3 uses [?] for wildcard type args, but [_] also works in this context
          stats += s"  val ${field.safeName}$$Conversion: org.apache.avro.Conversion[_] = new $cls()"
        case None => // skip
      }
    }

    s"object $name {\n${stats.mkString("\n")}\n}"
  }

  private def toThis(fields: List[Schema.Field]): String = {
    def defaultForType(schema: Schema): String = schema.getType match {
      case INT | LONG | FLOAT | DOUBLE => "0"
      case BOOLEAN => "false"
      case STRING => "\"\""
      case BYTES => "Array[Byte]()"
      case RECORD | FIXED => s"new ${schema.getFullName}()"
      case ARRAY => "List.empty"
      case MAP => "Map.empty"
      case UNION =>
        val types = schema.getTypes.asScala.toList
        if (!types.exists(_.getType == NULL)) logical(schema).getOrElse(defaultForType(types.head))
        else "None"
      case _ => "null"
    }

    def logical(schema: Schema): Option[String] = {
      if (ltc.logicalTypeInUse(schema)) {
        Some(ltc.getDefault(schema))
      } else None
    }

    s"def this() = this(${fields.map(f => logical(f.schema()).getOrElse(defaultForType(f.schema()))).mkString(", ")})"
  }
}
