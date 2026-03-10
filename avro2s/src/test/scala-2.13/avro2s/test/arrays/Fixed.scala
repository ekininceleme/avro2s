/** GENERATED CODE */

package avro2s.test.arrays
import org.apache.avro.Schema
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter, SpecificFixed}
case class Fixed() extends SpecificFixed {
  override def getSchema: Schema = Fixed.SCHEMA$
  override def readExternal(in: java.io.ObjectInput): Unit = {
    avro2s.test.arrays.Fixed.READER$.read(this, SpecificData.getDecoder(in))
    ()
  }
  override def writeExternal(out: java.io.ObjectOutput): Unit = avro2s.test.arrays.Fixed.WRITER$.write(this, SpecificData.getEncoder(out))
}
object Fixed {
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"Fixed\",\"namespace\":\"avro2s.test.arrays\",\"size\":2}")
  val READER$ = new SpecificDatumReader[Fixed](Fixed.SCHEMA$, Fixed.SCHEMA$, new SpecificData())
  val WRITER$ = new SpecificDatumWriter[Fixed](Fixed.SCHEMA$, new SpecificData())
  def apply(data: Array[Byte]): Fixed = {
    val fixed = new avro2s.test.arrays.Fixed()
    fixed.bytes(data)
    fixed
  }
}