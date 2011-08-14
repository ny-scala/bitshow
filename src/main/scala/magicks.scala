package bitshow

object OneMagic extends Converter {
  val name = "Identity Magick"
  def apply(bytes: Array[Byte]) = bytes
}
