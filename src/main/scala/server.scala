package bitshow

import unfiltered.request._
import unfiltered.response._

import org.clapper.avsl.Logger

/** embedded server */
object Server {
  val logger = Logger(getClass)
  def main(args: Array[String]) {
    unfiltered.jetty.Http.anylocal.context("/assets") { ctx =>
      ctx.resources(new java.net.URL(
        getClass.getResource("/www/css"), "."))
    }.filter(Browse)
    .run({ server =>
      unfiltered.util.Browser.open(server.url)
    })
  }
}
