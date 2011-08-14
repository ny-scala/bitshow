package bitshow

trait Converter extends (Array[Byte] => Array[Byte])

object Coverters {
  val all =  Map(
    "Identity Renderer" -> OneRenderer,
    "Identity Magic" -> OneMagic
  )
  def apply(named: String): Option[Converter] = all.get(named)
}
