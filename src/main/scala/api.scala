package bitshow

import unfiltered.request._
import unfiltered.response._

import net.liftweb.json.JsonDSL._

import org.clapper.avsl.Logger

/** This filter should handle conversions requested
 * by the client page, serve listings of available
 * images, and serve the images themselves */
object API extends unfiltered.filter.Plan {
  val logger = Logger(getClass)

  def intent = {
    case POST(Path("/convert")) =>
      logger.debug("Convert request")
      Json(("hello" -> "world") ~ ("id" -> "something") )
  }
}
