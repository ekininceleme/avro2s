/** GENERATED CODE */

package avro2s.test.arrays
import org.apache.avro.Schema
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter, SpecificFixed}
case class FixedB() extends SpecificFixed {
  override def getSchema: Schema = FixedB.SCHEMA$
  override def readExternal(in: java.io.ObjectInput): Unit = {
    avro2s.test.arrays.FixedB.READER$.read(this, SpecificData.getDecoder(in))
    ()
  }
  override def writeExternal(out: java.io.ObjectOutput): Unit = avro2s.test.arrays.FixedB.WRITER$.write(this, SpecificData.getEncoder(out))
}
object FixedB {
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"FixedB\",\"namespace\":\"avro2s.test.arrays\",\"size\":2}")
  val READER$ = new SpecificDatumReader[FixedB](FixedB.SCHEMA$, FixedB.SCHEMA$, new SpecificData())
  val WRITER$ = new SpecificDatumWriter[FixedB](FixedB.SCHEMA$, new SpecificData())
  def apply(data: Array[Byte]): FixedB = {
    val fixed = new avro2s.test.arrays.FixedB()
    fixed.bytes(data)
    fixed
  }
}