package bitshow

object OneRenderer extends Converter {
  val name = "Identity renderer"
  def apply(bytes: Array[Byte]) = bytes
}
