package fr.jonathanjorand

import fr.jonathanjorand.GitlabRegistry.Environment
import fr.jonathanjorand.GitlabRegistry.LastDeployment
import fr.jonathanjorand.GitlabRegistry.Project
import fr.jonathanjorand.GitlabRegistry.User

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class GitlabRegistry(gitlabSpi: GitlabSpi)(implicit ec: ExecutionContext) {
  def project(query: String): Future[Project] = {
    gitlabSpi
      .getProject(query)
      .map(project =>
        Project(
          project.name,
          project.environments.map(env => {
            Environment(
              env.id,
              env.name,
              env.state,
              env.last_deployment.map(deployment => {
                LastDeployment(
                  deployment.sha,
                  deployment.created_at,
                  deployment.status,
                  deployment.ref,
                  User(
                    deployment.user.name,
                    deployment.user.avatar_url
                  )
                )
              })
            )
          })
        )
      )
  }
}

object GitlabRegistry {
  case class Project(
      name: String,
      environments: Seq[Environment]
  )

  case class Environment(
      id: Int,
      name: String,
      state: String,
      lastDeployment: Option[LastDeployment]
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
      avatarUrl: String
  )
}
