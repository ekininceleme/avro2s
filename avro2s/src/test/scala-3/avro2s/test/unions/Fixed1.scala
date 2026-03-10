/** GENERATED CODE */

package avro2s.test.unions
import org.apache.avro.Schema
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter, SpecificFixed}
case class Fixed1() extends SpecificFixed {
  override def getSchema: Schema = Fixed1.SCHEMA$
  override def readExternal(in: java.io.ObjectInput): Unit = {
    avro2s.test.unions.Fixed1.READER$.read(this, SpecificData.getDecoder(in))
    ()
  }
  override def writeExternal(out: java.io.ObjectOutput): Unit = avro2s.test.unions.Fixed1.WRITER$.write(this, SpecificData.getEncoder(out))
}
object Fixed1 {
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"Fixed1\",\"namespace\":\"avro2s.test.unions\",\"size\":1}")
  val READER$ = new SpecificDatumReader[Fixed1](Fixed1.SCHEMA$, Fixed1.SCHEMA$, new SpecificData())
  val WRITER$ = new SpecificDatumWriter[Fixed1](Fixed1.SCHEMA$, new SpecificData())
  def apply(data: Array[Byte]): Fixed1 = {
    val fixed = new avro2s.test.unions.Fixed1()
    fixed.bytes(data)
    fixed
  }
}