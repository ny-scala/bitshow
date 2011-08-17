package bitshow

trait Converter extends (Item => Item) {
  /** Some nice display name for your converter */
  def name: String
}

object Converters {
  def names: Iterable[String] = mapped.keys
  private val converterList =
    OneRenderer :: 
    OneMagic ::
    FlipRenderer ::
    FlipMagick ::
    Nil
  private val mapped =
    (Map.empty[String, Converter] /: converterList) { (map, conv) =>
      map + (conv.name -> conv)
    }
  def apply(named: String): Option[Converter] = mapped.get(named)
}
