package fr.jonathanjorand

import fr.jonathanjorand.MetricsRegistry.Metrics
import spray.json.RootJsonFormat

//#json-formats
import spray.json.DefaultJsonProtocol

object JsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val metricsJsonFormat: RootJsonFormat[Metrics] = jsonFormat1(Metrics)

}
//#json-formats
