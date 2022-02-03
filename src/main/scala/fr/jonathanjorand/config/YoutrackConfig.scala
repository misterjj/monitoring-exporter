package fr.jonathanjorand.config

import com.typesafe.config.Config

case class YoutrackConfig(
    token: String,
    host: String
)

object YoutrackConfig {
  def from(config: Config): YoutrackConfig = {
    YoutrackConfig(
      token = config.getString("token"),
      host = config.getString("host")
    )
  }
}
