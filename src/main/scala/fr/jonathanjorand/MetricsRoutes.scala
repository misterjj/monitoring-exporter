package fr.jonathanjorand

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class MetricsRoutes(youtrackRegistry: MetricsRegistry) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._

  def routes: Route = {
    pathPrefix("metrics") {
      get {
        parameters("q") { query: String =>
          complete(StatusCodes.OK, youtrackRegistry.get(query))
        }
      }
    }
  }
}
