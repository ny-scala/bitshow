package bitshow
import javax.imageio.ImageIO
import java.io._
import org.apache.commons.io.IOUtils
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp


object OneRenderer extends Converter {
  val name = "Identity renderer"
  def apply(item: Item) = item
}

object FlipRenderer extends Converter {

        def mimeToImageIOType(ty: String) = ty match {
          case "image/png" => "png"
          case "image/gif" => "gif"
          case "image/jpg" => "jpg"
          case _ => throw new UnsupportedOperationException(ty)
        }

	val name = "Flip image"
	def apply(item: Item) = {
		val image = ImageIO.read(new java.io.ByteArrayInputStream(item.bytes))
		val output = new java.io.ByteArrayOutputStream

    		val tx = AffineTransform.getScaleInstance(-1, 1)
    		tx.translate(-image.getWidth(null), 0)
    		val op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
    		val transformedImage = op.filter(image, null);

		ImageIO.write(transformedImage, mimeToImageIOType(item.contentType), output)
		Item(item.contentType, output.toByteArray())
	}

	def main(args: Array[String]) {
		val f = new File(args(0))
		val out = new File(args(1))
		val source = IOUtils.toByteArray(new FileInputStream(f))
		val result = FlipRenderer(Item("image/png", source))
		val outputFile = new FileOutputStream(out)
                outputFile.write(result.bytes)
           	outputFile.close
	}
}
