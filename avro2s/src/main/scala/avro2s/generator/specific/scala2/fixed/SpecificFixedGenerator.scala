package avro2s.generator.specific.scala2.fixed

import avro2s.generator.{AstBuilder, GeneratedCode}

import scala.meta._

private[avro2s] object SpecificFixedGenerator {

  def schemaToScala2Fixed(schema: org.apache.avro.Schema, namespace: Option[String]): GeneratedCode = {
    val name = schema.getName
    val ns = Option(schema.getNamespace).orElse(namespace)
    val nsPrefix = ns.map(_ + ".").getOrElse("")

    val body = List(
      AstBuilder.buildDef("getSchema", Nil, "org.apache.avro.Schema", s"$name.SCHEMA$$", isOverride = true),
      AstBuilder.buildDef(
        "readExternal",
        List(List(AstBuilder.DefParam("in", "java.io.ObjectInput"))),
        "Unit",
        s"""{
           |$nsPrefix$name.READER$$.read(this, org.apache.avro.specific.SpecificData.getDecoder(in))
           |()
           |}""".stripMargin,
        isOverride = true
      ),
      AstBuilder.buildDef(
        "writeExternal",
        List(List(AstBuilder.DefParam("out", "java.io.ObjectOutput"))),
        "Unit",
        s"$nsPrefix$name.WRITER$$.write(this, org.apache.avro.specific.SpecificData.getEncoder(out))",
        isOverride = true
      )
    )
    val classDef = AstBuilder.buildCaseClass(name, Nil, List("org.apache.avro.specific.SpecificFixed"), body)

    val schemaJson = schema.toString.replace("\"", "\\\"")
    val objectDef = AstBuilder.buildObject(name, List(
      AstBuilder.buildVal("SCHEMA$", None, s"""new org.apache.avro.Schema.Parser().parse("$schemaJson")"""),
      AstBuilder.buildVal("READER$", None, s"new org.apache.avro.specific.SpecificDatumReader[$name]($name.SCHEMA$$, $name.SCHEMA$$, new org.apache.avro.specific.SpecificData())"),
      AstBuilder.buildVal("WRITER$", None, s"new org.apache.avro.specific.SpecificDatumWriter[$name]($name.SCHEMA$$, new org.apache.avro.specific.SpecificData())"),
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

    val code = AstBuilder.renderFile(ns, "/** GENERATED CODE */", Nil, List(classDef, objectDef))
    GeneratedCode(s"${ns.map(_.replace(".", "/") + "/").getOrElse("") + name}.scala", code)
  }
}
