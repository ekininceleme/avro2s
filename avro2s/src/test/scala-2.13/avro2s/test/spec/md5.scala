/** GENERATED CODE */

package avro2s.test.spec
import org.apache.avro.Schema
import org.apache.avro.specific.{SpecificData, SpecificDatumReader, SpecificDatumWriter, SpecificFixed}
case class md5() extends SpecificFixed {
  override def getSchema: Schema = md5.SCHEMA$
  override def readExternal(in: java.io.ObjectInput): Unit = {
    avro2s.test.spec.md5.READER$.read(this, SpecificData.getDecoder(in))
    ()
  }
  override def writeExternal(out: java.io.ObjectOutput): Unit = avro2s.test.spec.md5.WRITER$.write(this, SpecificData.getEncoder(out))
}
object md5 {
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"fixed\",\"name\":\"md5\",\"namespace\":\"avro2s.test.spec\",\"size\":16}")
  val READER$ = new SpecificDatumReader[md5](md5.SCHEMA$, md5.SCHEMA$, new SpecificData())
  val WRITER$ = new SpecificDatumWriter[md5](md5.SCHEMA$, new SpecificData())
  def apply(data: Array[Byte]): md5 = {
    val fixed = new avro2s.test.spec.md5()
    fixed.bytes(data)
    fixed
  }
}