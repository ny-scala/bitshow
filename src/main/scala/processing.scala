package bitshow

import processing.core._
import PConstants._
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

object GraySquare extends Converter {
  val name = "put a black square on it"
  def apply(item: Item) = {
    val inImg = {
      val in = new java.io.ByteArrayInputStream(item.bytes)
      val bimg = ImageIO.read(in)
      val img = new PImage(bimg.getWidth, bimg.getHeight, ARGB)
      bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width)
      img.updatePixels()
      img
    }
    object Applet extends PApplet {
      val buf = createGraphics(700, 300, P3D)
      buf.beginDraw()
      buf.noStroke()
      buf.fill(255)
      buf.rect(0, 0, buf.width, buf.height)
      buf.image(inImg, -50, 0)
      buf.fill(100)
      buf.rect(0, 0, 100, 100)
      buf.endDraw()
    }
    val img = Applet.buf.getImage.asInstanceOf[BufferedImage]
    val out = new java.io.ByteArrayOutputStream
    val writer = ImageIO.write(img, "png", out)
    Item("image/png", out.toByteArray())
  }
}
