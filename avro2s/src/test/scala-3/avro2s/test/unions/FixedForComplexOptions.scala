/** GENERATED CODE */

package avro2s.test.unions
import org.apache.avro.Schema
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter, SpecificFixed}
case class FixedForComplexOptions() extends SpecificFixed {
  override def getSchema: Schema = FixedForComplexOptions.SCHEMA$
  override def readExternal(in: java.io.ObjectInput): Unit = {
    avro2s.test.unions.FixedForComplexOptions.READER$.read(this, SpecificData.getDecoder(in))
    ()
  }
  override def writeExternal(out: java.io.ObjectOutput): Unit = avro2s.test.unions.FixedForComplexOptions.WRITER$.write(this, SpecificData.getEncoder(out))
}
object FixedForComplexOptions {
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"FixedForComplexOptions\",\"namespace\":\"avro2s.test.unions\",\"size\":16}")
  val READER$ = new SpecificDatumReader[FixedForComplexOptions](FixedForComplexOptions.SCHEMA$, FixedForComplexOptions.SCHEMA$, new SpecificData())
  val WRITER$ = new SpecificDatumWriter[FixedForComplexOptions](FixedForComplexOptions.SCHEMA$, new SpecificData())
  def apply(data: Array[Byte]): FixedForComplexOptions = {
    val fixed = new avro2s.test.unions.FixedForComplexOptions()
    fixed.bytes(data)
    fixed
  }
}