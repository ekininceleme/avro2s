/** GENERATED CODE */

package avro2s.test.unions
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class RecordForComplexOptions(var field1: String) extends SpecificRecordBase {
  def this() = this("")
  override def getSchema: Schema = RecordForComplexOptions.SCHEMA$
  override def get(field$ : Int): AnyRef = {
    (field$ : @switch) match {
      case 0 =>
        field1.asInstanceOf[AnyRef]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
  override def put(field$ : Int, value: Any): Unit = {
    (field$ : @switch) match {
      case 0 =>
        this.field1 = value.toString.asInstanceOf[String]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object RecordForComplexOptions {
  val SCHEMA$ : Schema =
    new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"RecordForComplexOptions\",\"namespace\":\"avro2s.test.unions\",\"fields\":[{\"name\":\"field1\",\"type\":\"string\"}]}")
}