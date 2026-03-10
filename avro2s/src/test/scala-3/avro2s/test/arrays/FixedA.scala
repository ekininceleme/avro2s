/** GENERATED CODE */

package avro2s.test.arrays
import org.apache.avro.Schema
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter, SpecificFixed}
case class FixedA() extends SpecificFixed {
  override def getSchema: Schema = FixedA.SCHEMA$
  override def readExternal(in: java.io.ObjectInput): Unit = {
    avro2s.test.arrays.FixedA.READER$.read(this, SpecificData.getDecoder(in))
    ()
  }
  override def writeExternal(out: java.io.ObjectOutput): Unit = avro2s.test.arrays.FixedA.WRITER$.write(this, SpecificData.getEncoder(out))
}
object FixedA {
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"FixedA\",\"namespace\":\"avro2s.test.arrays\",\"size\":2}")
  val READER$ = new SpecificDatumReader[FixedA](FixedA.SCHEMA$, FixedA.SCHEMA$, new SpecificData())
  val WRITER$ = new SpecificDatumWriter[FixedA](FixedA.SCHEMA$, new SpecificData())
  def apply(data: Array[Byte]): FixedA = {
    val fixed = new avro2s.test.arrays.FixedA()
    fixed.bytes(data)
    fixed
  }
}