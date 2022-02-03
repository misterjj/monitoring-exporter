package fr.jonathanjorand.config

import com.typesafe.config.Config

case class MainConfig (
    youtrackConfig: YoutrackConfig
                      )

object MainConfig {
  def from(config: Config):MainConfig = {
    MainConfig(
      youtrackConfig = YoutrackConfig.from(config.getConfig("youtrack"))
    )
  }
}
