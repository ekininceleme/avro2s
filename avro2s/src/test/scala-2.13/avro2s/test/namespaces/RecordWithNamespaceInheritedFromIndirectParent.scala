/** GENERATED CODE */

package avro2s.test.namespaces
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class RecordWithNamespaceInheritedFromIndirectParent(var _string: String) extends SpecificRecordBase {
  def this() = this("")
  override def getSchema: Schema = RecordWithNamespaceInheritedFromIndirectParent.SCHEMA$
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
        this._string = value.toString.asInstanceOf[String]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object RecordWithNamespaceInheritedFromIndirectParent {
  val SCHEMA$ : Schema = new Schema.Parser()
    .parse("{\"type\":\"record\",\"name\":\"RecordWithNamespaceInheritedFromIndirectParent\",\"namespace\":\"avro2s.test.namespaces\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"}]}")
}