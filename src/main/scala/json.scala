package bitshow

object LazyJson {
  import net.liftweb.json._
  import net.liftweb.json.Extraction._
  /* Hintless format. Used by decompose, which will end up doing reflective footwork. */
  implicit val formats = Serialization.formats(NoTypeHints)
  /* Pimp anything with toJson using a reflective conversion. */
  implicit def any2JValue(any: Any): AnyPimp = new AnyPimp(any)
  
  class AnyPimp(a: Any) {
    def toJson = decompose(a)
  }
}
