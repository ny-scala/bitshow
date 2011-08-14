package bitshow

trait Storage {
  def get(id: String): Option[Array[Byte]]
  def put(bytes: Array[Byte]): String
}

object DefaultStore extends VectorStore

trait VectorStore { self =>
  private var vector = Vector.empty[Array[Byte]]
  def get(id: String) = self.synchronized {
    import scala.util.control.Exception._
    allCatch.opt { vector(id.toInt) }
  }
  def put(bytes: Array[Byte]) =
    self.synchronized {
      vector = vector :+ bytes
      (vector.length - 1).toString
    }
}  
