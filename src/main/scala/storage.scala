package bitshow

import java.lang.IllegalStateException
import java.io.ByteArrayOutputStream

case class Item(contentType: String, bytes: Array[Byte])

trait Storage {
  def get(id: String): Option[Item]
  def put(item: Item): String
}

object DefaultStore extends MongoGridFSStorage

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
}

trait MongoGridFSStorage extends Storage { self =>
  import com.mongodb.casbah.Imports._
  import com.mongodb.casbah.gridfs.Imports._

  lazy val mongo = MongoConnection()("bitshow")

  lazy val gridfs = GridFS(mongo, "images")

  def get(id: String): Option[Item] = {
    import scala.util.control.Exception._
    allCatch.opt { new ObjectId(id) } match {
      case Some(oid: ObjectId) => gridfs.findOne(oid) match {
        case Some(file) => allCatch.opt {
          val bos = new ByteArrayOutputStream(file.length.toInt)
          file.writeTo(bos)
          val bytes = bos.toByteArray
          bos.close()
          Item(file.contentType.getOrElse("unknown_contentType"), bytes)
        }
        case None =>
          println("No matching file for provided ID '%s'" format oid)
          None
      }
      case None =>
        println("Invalid ID. Stop screwing around.")
        None
    }
  }


  def put(item: Item): String = {
    require(item.contentType != null && !item.contentType.isEmpty,
            "Content Type may not be null or empty.")
    require(item.bytes != null && item.bytes.size > 0,
            "Why would I want to save an empty Byte Array?")

    item match {
      case Item("image/png", _) =>
        println("Saving a PNG")
      case Item("image/jpeg", _) =>
        println("Saving a JPEG")
      case Item("image/ascii-art", _) =>
        println("Saving ASCII Art.")
      case Item("image/gif", _) =>
        println("Saving a GIF")
      case Item(unknown, _) =>
        println("Saving an image with Content Type '%s'" format unknown)
    }

    val id: Option[AnyRef] = gridfs(item.bytes) { fh =>
      fh.contentType = item.contentType
    }

    id match {
      case Some(oid: ObjectId) =>
        oid.toString()
      case Some(other) =>
        throw new IllegalArgumentException("Unknown ID Type '%s'" format other)
      case None =>
        throw new IllegalStateException("Possible failure to save file, no ID was generated/returned.")
    }
  }


  /** Quick demonstration of pattern matching for class extraction from a less specific type */
/*  def put(item: AnyRef) = item match {
    case bytes: Array[Byte] =>

    case stream: java.io.InputStream =>

    case file: scala.io.File =>

    case default =>
      (" I don't know what the hell a class of type '%s' is " format default)

  }*/
}

