package avro2s.generator.specific.scala2.fixed

import avro2s.generator.{AstBuilder, GeneratedCode}

import scala.meta._

private[avro2s] object SpecificFixedGenerator {

  def schemaToScala2Fixed(schema: org.apache.avro.Schema, namespace: Option[String]): GeneratedCode = {
    val name = schema.getName
    val ns = Option(schema.getNamespace).orElse(namespace)
    val nsPrefix = ns.map(_ + ".").getOrElse("")

    val imports = List(
      "org.apache.avro.Schema",
      "org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter, SpecificFixed}"
    )

    val body = List(
      AstBuilder.buildDef("getSchema", Nil, "Schema", s"$name.SCHEMA$$", isOverride = true),
      AstBuilder.buildDef(
        "readExternal",
        List(List(AstBuilder.DefParam("in", "java.io.ObjectInput"))),
        "Unit",
        s"""{
           |$nsPrefix$name.READER$$.read(this, SpecificData.getDecoder(in))
           |()
           |}""".stripMargin,
        isOverride = true
      ),
      AstBuilder.buildDef(
        "writeExternal",
        List(List(AstBuilder.DefParam("out", "java.io.ObjectOutput"))),
        "Unit",
        s"$nsPrefix$name.WRITER$$.write(this, SpecificData.getEncoder(out))",
        isOverride = true
      )
    )
    val classDef = AstBuilder.buildCaseClass(name, Nil, List("SpecificFixed"), body)

    val schemaJson = schema.toString.replace("\"", "\\\"")
    val objectDef = AstBuilder.buildObject(name, List(
      AstBuilder.buildVal("SCHEMA$", None, s"""new Schema.Parser().parse("$schemaJson")"""),
      AstBuilder.buildVal("READER$", None, s"new SpecificDatumReader[$name]($name.SCHEMA$$, $name.SCHEMA$$, new SpecificData())"),
      AstBuilder.buildVal("WRITER$", None, s"new SpecificDatumWriter[$name]($name.SCHEMA$$, new SpecificData())"),
      AstBuilder.buildDef(
        "apply",
        List(List(AstBuilder.DefParam("data", "Array[Byte]"))),
        name,
        s"""{
           |val fixed = new $nsPrefix$name()
           |fixed.bytes(data)
           |fixed
           |}""".stripMargin
      )
    ))

    val code = AstBuilder.renderFile(ns, "/** GENERATED CODE */", imports, List(classDef, objectDef))
    GeneratedCode(s"${ns.map(_.replace(".", "/") + "/").getOrElse("") + name}.scala", code)
  }
}
