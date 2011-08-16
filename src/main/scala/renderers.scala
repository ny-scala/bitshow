package bitshow

object OneRenderer extends Converter {
  val name = "Identity renderer"
  def apply(item: Item) = item
}
