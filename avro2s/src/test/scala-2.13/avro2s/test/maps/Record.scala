/** GENERATED CODE */

package avro2s.test.maps
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class Record(var a: String) extends SpecificRecordBase {
  def this() = this("")
  override def getSchema: Schema = Record.SCHEMA$
  override def get(field$ : Int): AnyRef = {
    (field$ : @switch) match {
      case 0 =>
        a.asInstanceOf[AnyRef]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
  override def put(field$ : Int, value: Any): Unit = {
    (field$ : @switch) match {
      case 0 =>
        this.a = value.toString.asInstanceOf[String]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object Record { val SCHEMA$ : Schema = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Record\",\"namespace\":\"avro2s.test.maps\",\"fields\":[{\"name\":\"a\",\"type\":\"string\"}]}") }