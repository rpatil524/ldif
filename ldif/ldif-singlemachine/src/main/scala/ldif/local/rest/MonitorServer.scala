package ldif.local.rest

/**
 * Created by IntelliJ IDEA.
 * User: andreas
 * Date: 2/21/12
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */

import javax.ws.rs._
import com.sun.jersey.api.container.httpserver.HttpServerFactory
import core.Response
import ldif.util.StatusMonitor
import com.sun.net.httpserver.HttpServer

@Path("/")
class MonitorServer {
  @GET @Produces(Array[String]("text/plain"))
  def getText(): String = {
    MonitorServer.generalStatusMonitor.getText
  }

  @GET @Produces(Array[String]("text/html"))
  def getHtml(): String = {
    MonitorServer.generalStatusMonitor.getHtml
  }
}

object MonitorServer {
  val generalStatusMonitor: StatusMonitor = dummyStatusMonitor
  private var server: HttpServer = null

  def stop() {
    server.stop(0)
  }

  def start(uri: String) {
    if(server!=null)
      stop()
    server = HttpServerFactory.create(uri)
    server.start()
  }

  def main(args: Array[String]) {
    MonitorServer.start("http://160.45.137.77:9999/")
    while(true) {

    }
  }
}

object dummyStatusMonitor extends StatusMonitor {
  def getHtml = "<html><head><title>No status monitor found</title></head><body><b>If you see this message then there is no status monitor implemented, yet, for this URI!</b></body></html>"

  def getText = "If you see this message then there is no status monitor implemented, yet, for this URI."
}