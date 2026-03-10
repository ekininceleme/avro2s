/** GENERATED CODE */

package avro2s.test.namespaces
import scala.annotation.switch
import org.apache.avro.{AvroRuntimeException, Conversion, Schema}
import org.apache.avro.specific.{SpecificData, SpecificRecordBase}
case class Namespaces(
    var _record_with_explicit_namespace: avro2s.test.namespaces.explicit.RecordWithExplicitNamespace,
    var _record_with_inherited_namespace: avro2s.test.namespaces.RecordWithInheritedNamespace
) extends SpecificRecordBase {
  def this() = this(new avro2s.test.namespaces.explicit.RecordWithExplicitNamespace(), new avro2s.test.namespaces.RecordWithInheritedNamespace())
  override def getSchema: Schema = Namespaces.SCHEMA$
  override def get(field$ : Int): AnyRef = {
    (field$ : @switch) match {
      case 0 =>
        _record_with_explicit_namespace.asInstanceOf[AnyRef]
      case 1 =>
        _record_with_inherited_namespace.asInstanceOf[AnyRef]
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
  override def put(field$ : Int, value: Any): Unit = {
    (field$ : @switch) match {
      case 0 =>
        this._record_with_explicit_namespace = {
          value.asInstanceOf[avro2s.test.namespaces.explicit.RecordWithExplicitNamespace]
        }
      case 1 =>
        this._record_with_inherited_namespace = {
          value.asInstanceOf[avro2s.test.namespaces.RecordWithInheritedNamespace]
        }
      case _ =>
        throw new AvroRuntimeException("Bad index")
    }
  }
}
object Namespaces {
  val SCHEMA$ : Schema = new Schema.Parser().parse(
    "{\"type\":\"record\",\"name\":\"Namespaces\",\"namespace\":\"avro2s.test.namespaces\",\"fields\":[{\"name\":\"_record_with_explicit_namespace\",\"type\":{\"type\":\"record\",\"name\":\"RecordWithExplicitNamespace\",\"namespace\":\"avro2s.test.namespaces.explicit\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"},{\"name\":\"_record_with_namespace_inherited_from_direct_parent\",\"type\":{\"type\":\"record\",\"name\":\"RecordWithNamespaceInheritedFromDirectParent\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"},{\"name\":\"_record_with_namespace_inherited_from_indirect_non_top_level_parent\",\"type\":{\"type\":\"record\",\"name\":\"RecordWithNamespaceInheritedFromIndirectNonTopLevelParent\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"}]}}]}},{\"name\":\"_array_of_records\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"RecordWithNamespaceInheritedViaArray\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"}]}}},{\"name\":\"_map_of_records\",\"type\":{\"type\":\"map\",\"values\":{\"type\":\"record\",\"name\":\"RecordWithNamespaceInheritedViaMap\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"}]}}},{\"name\":\"_union_of_records\",\"type\":[{\"type\":\"record\",\"name\":\"RecordWithNamespaceInheritedViaUnion\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"}]},\"string\"]}]}},{\"name\":\"_record_with_inherited_namespace\",\"type\":{\"type\":\"record\",\"name\":\"RecordWithInheritedNamespace\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"},{\"name\":\"_record_with_namespace_inherited_from_indirect_parent\",\"type\":{\"type\":\"record\",\"name\":\"RecordWithNamespaceInheritedFromIndirectParent\",\"fields\":[{\"name\":\"_string\",\"type\":\"string\"}]}}]}}]}"
  )
}