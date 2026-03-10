package avro2s.generator.specific.scala2.fixed

import avro2s.generator.{AstBuilder, GeneratedCode}

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

    // Render companion object as raw string to preserve triple-quoted schema string
    val objectStr =
      s"""object $name {
         |  val SCHEMA$$ = new org.apache.avro.Schema.Parser().parse(\"\"\"${schema.toString}\"\"\")
         |  val READER$$ = new org.apache.avro.specific.SpecificDatumReader[$name]($name.SCHEMA$$, $name.SCHEMA$$, new org.apache.avro.specific.SpecificData())
         |  val WRITER$$ = new org.apache.avro.specific.SpecificDatumWriter[$name]($name.SCHEMA$$, new org.apache.avro.specific.SpecificData())
         |  def apply(data: Array[Byte]): $name = {
         |    val fixed = new $nsPrefix$name()
         |    fixed.bytes(data)
         |    fixed
         |  }
         |}""".stripMargin

    val code = AstBuilder.renderFileWithRawParts(ns, "/** GENERATED CODE */", Nil, List(classDef), List(objectStr))
    GeneratedCode(s"${ns.map(_.replace(".", "/") + "/").getOrElse("") + name}.scala", code)
  }
}
