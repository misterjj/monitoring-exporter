package fr.jonathanjorand

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class YoutrackRoutes(youtrackRegistry: YoutrackRegistry) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._

  def routes: Route = {
    pathPrefix("youtrack" / "metrics") {
      get {
        parameters("q") { query: String =>
          complete(StatusCodes.OK, youtrackRegistry.get(query))
        }
      }
    }
  }
}
