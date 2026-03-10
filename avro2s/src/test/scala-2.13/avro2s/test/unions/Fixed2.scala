/** GENERATED CODE */

package avro2s.test.unions
import org.apache.avro.Schema
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter, SpecificFixed}
case class Fixed2() extends SpecificFixed {
  override def getSchema: Schema = Fixed2.SCHEMA$
  override def readExternal(in: java.io.ObjectInput): Unit = {
    avro2s.test.unions.Fixed2.READER$.read(this, SpecificData.getDecoder(in))
    ()
  }
  override def writeExternal(out: java.io.ObjectOutput): Unit = avro2s.test.unions.Fixed2.WRITER$.write(this, SpecificData.getEncoder(out))
}
object Fixed2 {
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"Fixed2\",\"namespace\":\"avro2s.test.unions\",\"size\":1}")
  val READER$ = new SpecificDatumReader[Fixed2](Fixed2.SCHEMA$, Fixed2.SCHEMA$, new SpecificData())
  val WRITER$ = new SpecificDatumWriter[Fixed2](Fixed2.SCHEMA$, new SpecificData())
  def apply(data: Array[Byte]): Fixed2 = {
    val fixed = new avro2s.test.unions.Fixed2()
    fixed.bytes(data)
    fixed
  }
}