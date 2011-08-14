package bitshow

trait Storage {
  def get(id: String): Option[Array[Byte]]
  def put(bytes: Array[Byte]): String
}

object VectorStore {
  private var vector = Vector.empty[Array[Byte]]
  def get(id: String) = VectorStore.synchronized {
    import scala.util.control.Exception._
    allCatch.opt { vector(id.toInt) }
  }
  def put(bytes: Array[Byte]) =
    VectorStore.synchronized {
      vector = vector :+ bytes
      (vector.length - 1).toString
    }
}  
