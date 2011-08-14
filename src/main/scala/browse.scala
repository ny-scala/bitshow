package bitshow

import unfiltered.request._
import unfiltered.response._

import org.clapper.avsl.Logger

/** Browser front end, serves the page and handles multipart uploads */
object Browse extends unfiltered.filter.Plan {
  val logger = Logger(getClass)

  def intent = {
    case GET(Path(p)) =>
      logger.debug("GET %s" format p)
      Ok ~> view(<p> bitshow </p>)
  }
  def view(body: scala.xml.NodeSeq) = {
    Html(
     <html>
      <head>
        <title>bitshow</title>
        <link rel="stylesheet" type="text/css" href="/assets/css/app.css"/>
        <script src="/assets/js/jquery-1.6.2.min.js" />
      </head>
      <body>
       <div id="container">
       { body }
       </div>
     </body>
    </html>
   )
  }
}
