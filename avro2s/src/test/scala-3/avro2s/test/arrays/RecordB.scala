/** GENERATED CODE */

package avro2s.test.arrays
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class RecordB(var _string: String) extends SpecificRecordBase {
  def this() = this("")
  override def getSchema: Schema = RecordB.SCHEMA$
  override def get(field$ : Int): AnyRef = {
    (field$ : @switch) match {
      case 0 =>
        _string.asInstanceOf[AnyRef]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
  override def put(field$ : Int, value: Any): Unit = {
    (field$ : @switch) match {
      case 0 =>
        this._string = {
          value.toString.asInstanceOf[String]
        }
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object RecordB {
  val SCHEMA$ : Schema = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"RecordB\",\"namespace\":\"avro2s.test.arrays\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"}]}")
}