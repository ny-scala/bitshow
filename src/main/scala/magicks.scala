package bitshow

object OneMagic extends Converter {
  val name = "Identity Magick"
  def apply(item: Item) = item
}
