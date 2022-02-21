package fr.jonathanjorand

import fr.jonathanjorand.YoutrackMetricsRegistry.Metrics

import scala.concurrent.{ExecutionContext, Future}

class YoutrackMetricsRegistry(youtrackSpi: YoutrackSpi)(implicit ec: ExecutionContext) {
  def get(query: String): Future[Metrics] = {
    youtrackSpi.getCount(query).map(a => Metrics(a))
  }
}

object YoutrackMetricsRegistry {
  case class Metrics(
      nb: Int
  )
}
