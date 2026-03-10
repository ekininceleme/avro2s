package avro2s.generator.javagenerator

import avro2s.generator.GeneratedCode
import org.apache.commons.text.StringEscapeUtils

import scala.jdk.CollectionConverters._

private[avro2s] object JavaGenericEnumGenerator {
  def schemaToJavaEnum(schema: org.apache.avro.Schema, namespace: Option[String]): GeneratedCode = {
    val name = schema.getName
    val enumSymbols = schema.getEnumSymbols.asScala
    val ns = Option(schema.getNamespace).orElse(namespace)
    val nsString = ns.getOrElse("")

    val pkg = if (ns.isDefined) s"package $nsString;\n\n" else ""

    val code =
      s"""/** GENERATED CODE */
         |
         |${pkg}public enum $name implements org.apache.avro.generic.GenericEnumSymbol<$name> {
         |  ${enumSymbols.mkString(", ")};
         |
         |  public static final org.apache.avro.Schema SCHEMA$$ = new org.apache.avro.Schema.Parser().parse("${StringEscapeUtils.escapeJava(schema.toString)}");
         |  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$$; }
         |
         |  @Override
         |  public org.apache.avro.Schema getSchema() { return SCHEMA$$; }
         |}""".stripMargin

    GeneratedCode(s"${ns.map(_.replace(".", "/") + "/").getOrElse("") + name}.java", code)
  }
}
