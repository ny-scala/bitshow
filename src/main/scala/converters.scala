package bitshow

trait Converter extends (Array[Byte] => Array[Byte]) {
  /** Some nice display name for your converter */
  def name: String
}

object Converters {
  def names: Iterable[String] = mapped.keys
  private val converterList =
    OneRenderer :: 
    OneMagic ::
    Nil
  private val mapped =
    (Map.empty[String, Converter] /: converterList) { (map, conv) =>
      map + (conv.name -> conv)
    }
  def apply(named: String): Option[Converter] = mapped.get(named)
}
