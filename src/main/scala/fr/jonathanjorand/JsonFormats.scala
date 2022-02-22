package fr.jonathanjorand

import fr.jonathanjorand.GitlabRegistry.Environment
import fr.jonathanjorand.GitlabRegistry.LastDeployment
import fr.jonathanjorand.GitlabRegistry.Project
import fr.jonathanjorand.GitlabRegistry.User
import fr.jonathanjorand.YoutrackRegistry.Metrics
import spray.json.RootJsonFormat

//#json-formats
import spray.json.DefaultJsonProtocol

object JsonFormats {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val metricsJsonFormat: RootJsonFormat[Metrics] = jsonFormat1(Metrics)

  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat2(User)
  implicit val lastDeploymentJsonFormat: RootJsonFormat[LastDeployment] = jsonFormat5(LastDeployment)
  implicit val environmentJsonFormat: RootJsonFormat[Environment] = jsonFormat4(Environment)
  implicit val environmentsJsonFormat: RootJsonFormat[Project] = jsonFormat2(Project)

}
//#json-formats
