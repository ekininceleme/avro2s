package avro2s.generator.specific.scala2.fixed

import avro2s.generator.{AstBuilder, GeneratedCode}

import scala.meta._

private[avro2s] object SpecificFixedGenerator {

  def schemaToScala2Fixed(schema: org.apache.avro.Schema, namespace: Option[String]): GeneratedCode = {
    val name = schema.getName
    val ns = Option(schema.getNamespace).orElse(namespace)
    val nsPrefix = ns.map(_ + ".").getOrElse("")

    val classDef = AstBuilder.parseStat(
      s"""case class $name() extends org.apache.avro.specific.SpecificFixed {
         |  override def getSchema: org.apache.avro.Schema = $name.SCHEMA$$
         |  override def readExternal(in: java.io.ObjectInput): Unit = {
         |    $nsPrefix$name.READER$$.read(this, org.apache.avro.specific.SpecificData.getDecoder(in))
         |    ()
         |  }
         |  override def writeExternal(out: java.io.ObjectOutput): Unit = {
         |    $nsPrefix$name.WRITER$$.write(this, org.apache.avro.specific.SpecificData.getEncoder(out))
         |  }
         |}""".stripMargin
    )

    val schemaJson = schema.toString.replace("\"", "\\\"")
    val objectDef = AstBuilder.buildObject(name, List(
      AstBuilder.parseStat(s"""val SCHEMA$$ = new org.apache.avro.Schema.Parser().parse("$schemaJson")"""),
      AstBuilder.parseStat(s"""val READER$$ = new org.apache.avro.specific.SpecificDatumReader[$name]($name.SCHEMA$$, $name.SCHEMA$$, new org.apache.avro.specific.SpecificData())"""),
      AstBuilder.parseStat(s"""val WRITER$$ = new org.apache.avro.specific.SpecificDatumWriter[$name]($name.SCHEMA$$, new org.apache.avro.specific.SpecificData())"""),
      AstBuilder.parseStat(
        s"""def apply(data: Array[Byte]): $name = {
           |  val fixed = new $nsPrefix$name()
           |  fixed.bytes(data)
           |  fixed
           |}""".stripMargin
      )
    ))

    val code = AstBuilder.renderFile(ns, "/** GENERATED CODE */", Nil, List(classDef, objectDef))
    GeneratedCode(s"${ns.map(_.replace(".", "/") + "/").getOrElse("") + name}.scala", code)
  }
}
