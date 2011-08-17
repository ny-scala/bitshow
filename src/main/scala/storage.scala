package bitshow

case class Item(contentType: String, bytes: Array[Byte])

trait Storage {
  def get(id: String): Option[Item]
  def put(item: Item): String
  def list: Traversable[String]
}

object DefaultStore extends VectorStore

trait VectorStore extends Storage { self =>
  private var vector = Vector.empty[Item]
  def get(id: String) = self.synchronized {
    import scala.util.control.Exception._
    allCatch.opt { vector(id.toInt) }
  }
  def put(bytes: Item) =
    self.synchronized {
      vector = vector :+ bytes
      (vector.length - 1).toString
    }
  def list = (0 to vector.length).map { _.toString }
}
