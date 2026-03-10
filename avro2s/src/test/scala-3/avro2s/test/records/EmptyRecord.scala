/** GENERATED CODE */

package avro2s.test.records
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class EmptyRecord() extends SpecificRecordBase {
  override def getSchema: Schema = EmptyRecord.SCHEMA$
  override def get(field$ : Int): AnyRef = {
    (field$ : @switch) match {
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
  override def put(field$ : Int, value: Any): Unit = {
    (field$ : @switch) match {
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object EmptyRecord { val SCHEMA$ : Schema = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"EmptyRecord\",\"namespace\":\"avro2s.test.records\",\"fields\":[]}") }