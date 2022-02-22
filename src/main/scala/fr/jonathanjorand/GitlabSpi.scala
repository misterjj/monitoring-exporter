package fr.jonathanjorand

import cats.implicits._
import fr.jonathanjorand.GitlabSpi.Environment
import fr.jonathanjorand.GitlabSpi.EnvironmentFactory
import fr.jonathanjorand.GitlabSpi.Project
import fr.jonathanjorand.GitlabSpi.ProjectFactory
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

  def getProject(projectId: String): Future[Project] = {

    val path = s"/projects/$projectId"

    for {
      project <- callApi(path).map(res => ProjectFactory.toProject(res.body))
      envs    <- getEnvironments(projectId)
    } yield Project(
      project.name,
      envs
    )
  }
}

object GitlabSpi {
  case class Project(
      name: String,
      environments: Seq[Environment]
  )

  case class ProjectJson(
      name: String
  )

  object ProjectFactory {
    implicit val projectFormat: RootJsonFormat[ProjectJson] = jsonFormat1(ProjectJson)

    def toProject(string: String): ProjectJson = {
      string.parseJson.convertTo[ProjectJson]
    }
  }

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
