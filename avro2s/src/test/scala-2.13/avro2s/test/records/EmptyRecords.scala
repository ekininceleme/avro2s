/** GENERATED CODE */

package avro2s.test.records
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class EmptyRecords(var _string: String, var _empty_record: avro2s.test.records.EmptyRecord, var _int: Int) extends SpecificRecordBase {
  def this() = this("", new avro2s.test.records.EmptyRecord(), 0)
  override def getSchema: Schema = EmptyRecords.SCHEMA$
  override def get(field$ : Int): AnyRef = {
    (field$ : @switch) match {
      case 0 =>
        _string.asInstanceOf[AnyRef]
      case 1 =>
        _empty_record.asInstanceOf[AnyRef]
      case 2 =>
        _int.asInstanceOf[AnyRef]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
  override def put(field$ : Int, value: Any): Unit = {
    (field$ : @switch) match {
      case 0 =>
        this._string = value.toString.asInstanceOf[String]
      case 1 =>
        this._empty_record = value.asInstanceOf[avro2s.test.records.EmptyRecord]
      case 2 =>
        this._int = value.asInstanceOf[Int]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object EmptyRecords {
  val SCHEMA$ : Schema = new Schema.Parser().parse(
    "{\"type\":\"record\",\"name\":\"EmptyRecords\",\"namespace\":\"avro2s.test.records\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"},{\"name\":\"_empty_record\",\"type\":{\"type\":\"record\",\"name\":\"EmptyRecord\",\"fields\":[]}},{\"name\":\"_int\",\"type\":\"int\"}]}"
  )
}