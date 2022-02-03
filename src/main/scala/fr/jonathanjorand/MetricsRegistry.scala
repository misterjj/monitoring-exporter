package fr.jonathanjorand

import fr.jonathanjorand.MetricsRegistry.Metrics

import scala.concurrent.{ExecutionContext, Future}

class MetricsRegistry(youtrackSpi: YoutrackSpi)(implicit ec: ExecutionContext) {
  def get(query: String): Future[Metrics] = {
    youtrackSpi.getCount(query).map(a => Metrics(a))
  }
}

object MetricsRegistry {
  case class Metrics(
      nb: Int
  )
}
