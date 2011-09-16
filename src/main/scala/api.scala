package bitshow

import unfiltered.request._
import unfiltered.response._

import net.liftweb.json.JsonDSL._

import org.clapper.avsl.Logger
import java.net.URLDecoder.decode
import net.liftweb.json.JsonAST.{JString, JField, JObject}

/**This filter should handle conversions requested
 * by the client page, serve listings of available
 * images and converters, and serve the images themselves */
object API extends unfiltered.filter.Plan {
  val logger = Logger(getClass)

  def intent = {
    case req@POST(Path("/conversions")) =>
      JsonBody(req) match {
        case Some(JObject(JField("id", JString(id)) :: JField("converter", JString(converter)) :: Nil)) => {
          val converterOpt: Option[Converter] = Converters(decode(converter, "UTF-8"))
          val optionItem: Option[Item] = DefaultStore.get(id)
          converterOpt.flatMap(optionItem.map).map(DefaultStore.put).map(id => Json("id" -> id)).getOrElse(NotFound)
        }
        case _ => BadRequest
      }

    case GET(Path("/converters")) => Json(Converters.names)

    case GET(Path("/images")) => Json(DefaultStore.list)

    case GET(Path(Seg("images" :: id :: Nil))) =>
      DefaultStore.get(id).map(i => ContentType(i.contentType) ~> ResponseBytes(i.bytes)).getOrElse(NotFound)

    case GET(Path("/test")) =>
      val bytes = org.apache.commons.io.IOUtils.toByteArray(
        getClass.getResource("/www/img/ny-scala.png").openStream
      )
      val logo = Item("image/png", bytes)
      val item = GraySquare(logo)
      ContentType(item.contentType) ~> ResponseBytes(item.bytes)
  }
}
