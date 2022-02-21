package fr.jonathanjorand.config

import com.typesafe.config.Config

case class GitlabConfig(
    token: String,
    host: String
)

object GitlabConfig {
  def from(config: Config): GitlabConfig = {
    GitlabConfig(
      token = config.getString("token"),
      host = config.getString("host")
    )
  }
}
