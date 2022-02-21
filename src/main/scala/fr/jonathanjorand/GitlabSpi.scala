package fr.jonathanjorand

import cats.implicits._
import fr.jonathanjorand.GitlabSpi.Environment
import fr.jonathanjorand.GitlabSpi.EnvironmentFactory
import fr.jonathanjorand.config.GitlabConfig
import scalaj.http._
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GitlabSpi(config: GitlabConfig) {

  val apiPrefix = "api/v4"

  private def callApi(path: String): Future[HttpResponse[String]] = {
    Future {
      Http(config.host + "/" + apiPrefix + "/" + path)
        .header("Authorization", "Bearer " + config.token)
        .header("content-type", "application/json")
        .asString
    }
  }

  def getEnvironments(projectId: String): Future[Seq[Environment]] = {
    def withDeployment(environment: Environment): Future[Environment] = {
      val path = s"/projects/$projectId/environments/${environment.id}"

      callApi(path).map(res => EnvironmentFactory.toEnv(res.body))
    }

    val path = s"/projects/$projectId/environments"
    callApi(path).flatMap(res => EnvironmentFactory.toList(res.body).traverse(withDeployment))
  }
}

object GitlabSpi {
  case class Environment(
      id: Int,
      name: String,
      state: String,
      last_deployment: Option[LastDeployment]
  )

  case class LastDeployment(
      sha: String,
      created_at: String,
      status: String,
      ref: String,
      user: User
  )

  case class User(
      name: String,
      avatar_url: String
  )

  object EnvironmentFactory {
    implicit val userFormat: RootJsonFormat[User] = jsonFormat2(User)
    implicit val deployFormat: RootJsonFormat[LastDeployment] = jsonFormat5(LastDeployment)
    implicit val envFormat: RootJsonFormat[Environment] = jsonFormat4(Environment)

    def toList(string: String): List[Environment] = {
      string.parseJson.convertTo[List[Environment]]
    }
    def toEnv(string: String): Environment = {
      string.parseJson.convertTo[Environment]
    }
  }
}
