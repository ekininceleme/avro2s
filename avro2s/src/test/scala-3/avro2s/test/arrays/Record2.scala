/** GENERATED CODE */

package avro2s.test.arrays
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class Record2(var _string: String, var _int: Int) extends SpecificRecordBase {
  def this() = this("", 0)
  override def getSchema: Schema = Record2.SCHEMA$
  override def get(field$ : Int): AnyRef = {
    (field$ : @switch) match {
      case 0 =>
        _string.asInstanceOf[AnyRef]
      case 1 =>
        _int.asInstanceOf[AnyRef]
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
      case 1 =>
        this._int = {
          value.asInstanceOf[Int]
        }
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object Record2 {
  val SCHEMA$ : Schema = new Schema.Parser()
    .parse("{\"type\":\"record\",\"name\":\"Record2\",\"namespace\":\"avro2s.test.arrays\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"},{\"name\":\"_int\",\"type\":\"int\"}]}")
}