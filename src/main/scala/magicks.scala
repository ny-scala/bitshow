package bitshow

import java.io._
import scala.sys.process._

object OneMagic extends Converter {
  val name = "Identity Magick"
  def apply(item: Item) = item
}

/**
 * This is a Converter that calls out to ImageMagick to do its thing.
 * The `name` is a descriptive name for the web UI, the `command` is
 * the ImageMagick command that will be run.
 * 
 * Example:
 *   val converter = new MagickConverter("(no name)", "convert -flip")
 */
class MagickConverter(override val name: String, command: String) extends Converter {
  /**
   * Given an Item, returns a new Item with the converted image.
   */
  override def apply(item: Item) = {
    val input = new ByteArrayInputStream(item.bytes)
    val output = new ByteArrayOutputStream(item.bytes.size)

    process(input, output)

    Item("image/jpeg", output.toByteArray)
  }

  /**
   * Given an InputStream and an OutputStream, runs the command on
   * the InputStream and places the output in the OutputStream.
   */
  def process(input: InputStream, output: OutputStream): Unit = {
    val cmd = MagickConverter.MagickPath + "/" + command + " - jpg:-"
    val exitValue = (Process(cmd) #< input #> output !)

    if (exitValue != 0)
      throw new Exception
  }
}

object MagickConverter {
  // The default path is /usr/local/bin. You can override this with the
  // magick.path system property.
  val MagickPath = System.getProperty("magick.path", "/usr/local/bin")
}

/**
 * An example MagickConverter that flips an image.
 */
object FlipMagick extends MagickConverter("flip", "convert -flip")

/**
 * This is just a test harness for using images in the filesystem so we
 * don't have to wait for other teams to do file uploading.
 */
object FileMagickConverter {
  /**
   * Convert a file image on the file system with the given command,
   * write to the given file name.
   *
   * Example:
   *   convert("convert -flip", "/Users/jortiz/img.png", "/Users/jortiz/img-flip.jpg")
   */
  def convert(command: String, fileIn: String, fileOut: String): Unit = {
    val input = new FileInputStream(fileIn)
    val output = new FileOutputStream(fileOut)
    val converter = new MagickConverter("(no name)", command)
    converter.process(input, output)
    input.close()
    output.close()
  }
}
