/** GENERATED CODE */

package avro2s.test.unions
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class Record2(var field2: Long) extends SpecificRecordBase {
  def this() = this(0)
  override def getSchema: Schema = Record2.SCHEMA$
  override def get(field$ : Int): AnyRef = {
    (field$ : @switch) match {
      case 0 =>
        field2.asInstanceOf[AnyRef]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
  override def put(field$ : Int, value: Any): Unit = {
    (field$ : @switch) match {
      case 0 =>
        this.field2 = {
          value.asInstanceOf[Long]
        }
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object Record2 {
  val SCHEMA$ : Schema = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Record2\",\"namespace\":\"avro2s.test.unions\",\"fields\":[{\"name\":\"field2\",\"type\":\"long\"}]}")
}