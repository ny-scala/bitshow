package bitshow

trait Converter extends (Item => Item) {
  /** Some nice display name for your converter */
  def name: String
}

object Coverters {
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
