package fr.jonathanjorand

import fr.jonathanjorand.GitlabRegistry.Environment
import fr.jonathanjorand.GitlabRegistry.Environments
import fr.jonathanjorand.GitlabRegistry.LastDeployment
import fr.jonathanjorand.GitlabRegistry.User

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class GitlabRegistry(gitlabSpi: GitlabSpi)(implicit ec: ExecutionContext) {
  def environment(query: String): Future[Environments] = {
    gitlabSpi
      .getEnvironments(query)
      .map(envs =>
        Environments(
          envs.map(env => {
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

  case class Environments(
      Environments: Seq[Environment]
  )
}
