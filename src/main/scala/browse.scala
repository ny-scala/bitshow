package bitshow

import unfiltered.request._
import unfiltered.response._

import org.clapper.avsl.Logger
import java.io.{ByteArrayOutputStream, InputStream}


object Pipe {
  def apply(out: ByteArrayOutputStream)(in: InputStream) = {
    @annotation.tailrec
    def consume(buffer: Array[Byte]): ByteArrayOutputStream  = in.read(buffer, 0, buffer.length) match {
      case -1 =>
        out.flush()
        out
      case read =>
         out.write(buffer, 0, read)
         consume(buffer)
    }
    consume(new Array[Byte](1024*16))
  }
}

/** Browser front end, serves the page and handles multipart uploads */
object Browse extends unfiltered.filter.Plan {
  val logger = Logger(getClass)

  def intent = {
    case GET(Path("/")) =>
      logger.debug("GET /")
      Ok ~> view(<p><form enctype="multipart/form-data" action="/up" method="POST">
        <input type="file" name="f" />
        <input type="submit" value="post"/>
       </form></p>)("index")
       
    case POST(Path("/up") & MultiPart(req)) =>
       MultiPartParams.Streamed(req).files("f") match {
         case Seq(file, _*) =>
            val out = new ByteArrayOutputStream()
            file.stream(Pipe(out))
            val bytes = out.toByteArray
            val id = DefaultStore.put(Item(file.contentType, bytes))
            Redirect("/previews/%s" format id)
         case _ => BadRequest ~> ResponseString("file f required")
       }
       
     case GET(Path(Seg("previews" :: id :: Nil))) =>
        DefaultStore.get(id) match {
          case Some(item) => view(<p>
             spice it up or <a href="/">upload another</a>
             <img class="bits" id={id} src={"/images/%s" format id}/>
              <select id="converters"/>
            </p>)("previews")
          case _ => NotFound
        }
  }

  def view(body: scala.xml.NodeSeq)(js: String*) = {
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
       <script type="text/javascript" src="/assets/js/bitshow.js"/>
       { js.map { j => <script type="text/javascript" src={"/assets/js/%s.js".format(j) }/>} }
     </body>
    </html>
   )
  }
}
