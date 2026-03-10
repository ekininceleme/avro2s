/** GENERATED CODE */

package avro2s.test.unions
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class UnionOfPrimitives(var _primitives: Long | Boolean | Int) extends SpecificRecordBase {
  def this() = this(0)
  override def getSchema: Schema = UnionOfPrimitives.SCHEMA$
  override def get(field$ : Int): AnyRef = {
    (field$ : @switch) match {
      case 0 =>
        _primitives match {
          case x: Long =>
            x.asInstanceOf[AnyRef]
          case x: Boolean =>
            x.asInstanceOf[AnyRef]
          case x: Int =>
            x.asInstanceOf[AnyRef]
        }
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
  override def put(field$ : Int, value: Any): Unit = {
    (field$ : @switch) match {
      case 0 =>
        this._primitives = {
          value match {
            case x: Long =>
              x
            case x: Boolean =>
              x
            case x: Int =>
              x
            case _ =>
              throw new org.apache.avro.AvroRuntimeException("Unexpected type: " + value.getClass.getName)
          }
        }
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object UnionOfPrimitives {
  val SCHEMA$ : Schema = new Schema.Parser()
    .parse("{\"type\":\"record\",\"name\":\"UnionOfPrimitives\",\"namespace\":\"avro2s.test.unions\",\"fields\":[{\"name\":\"_primitives\",\"type\":[\"long\",\"boolean\",\"int\"]}]}")
}