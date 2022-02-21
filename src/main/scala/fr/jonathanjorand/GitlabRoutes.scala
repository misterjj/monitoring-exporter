package fr.jonathanjorand

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class GitlabRoutes(gitlabRegistry: GitlabRegistry){

  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  def routes: Route = {
    pathPrefix("gitlab" / "environments") {
      get {
        parameters("project") { project: String =>
          complete(StatusCodes.OK, gitlabRegistry.environment(project))
        }
      }
    }
  }
}
