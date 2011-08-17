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
      view(<p><form id="bitpusher" enctype="multipart/form-data" action="/up" method="POST">
        <label for="f">upload</label><input type="file" name="f" />
        <input type="submit" value="post"/>
       </form><h2>converts</h2><ul id="bitpics"></ul></p>)("index")

    case POST(Path("/up") & MultiPart(req)) =>
       MultiPartParams.Streamed(req).files("f") match {
         case Seq(f, _*) =>
            logger.debug("POST /up [ name: %s, contenType: %s]" format(f.name, f.contentType))
            val out = new ByteArrayOutputStream()
            f.stream(Pipe(out))
            val id = DefaultStore.put(Item(f.contentType, out.toByteArray))
            Redirect("/previews/%s" format id)
         case _ => BadRequest ~> ResponseString("file f required")
       }

     case GET(Path(Seg("previews" :: id :: Nil))) =>
        logger.debug("GET /previews/%s" format id)
        DefaultStore.get(id) match {
          case Some(item) => view(<p>
             spice it up or <a href="/">upload another</a>
             <form action="/convert">
              <input type="hidden" name="id" value={id}/>
              <select name="converter" id="converters"/>
             </form>
             <img class="bits" id={id} src={"/images/%s" format id}/>
            </p>)("previews")
          case _ => NotFound
        }
  }

  def view(body: scala.xml.NodeSeq)(js: String*) = {
    Html(
     <html>
      <head>
        <title>bitshow</title>
        <link href="http://fonts.googleapis.com/css?family=Cabin+Sketch:700" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" type="text/css" href="/assets/css/app.css"/>
        <script src="/assets/js/jquery-1.6.2.min.js" />
      </head>
      <body>
       <div id="container">
        <h1><a href="/">bitshow</a></h1>
       { body }
        <div id="foot">
         <span class="ny ny-1">|</span><span class="ny ny-2">|</span><span class="ny ny-3">|</span><span class="ny ny-4">|</span> <a href="http://meetu.ps/3myHr">ny</a>-<a rel="src" href="https://github.com/ny-scala/bitshow">scala</a>
        </div>
       </div>
       <script type="text/javascript" src="/assets/js/bitshow.js"/>
       { js.map { j => <script type="text/javascript" src={"/assets/js/%s.js".format(j) }/>} }
     </body>
    </html>
   )
  }
}
