package fr.jonathanjorand

import fr.jonathanjorand.YoutrackRegistry.Metrics

import scala.concurrent.{ExecutionContext, Future}

class YoutrackRegistry(youtrackSpi: YoutrackSpi)(implicit ec: ExecutionContext) {
  def get(query: String): Future[Metrics] = {
    youtrackSpi.getCount(query).map(a => Metrics(a))
  }
}

object YoutrackRegistry {
  case class Metrics(
      nb: Int
  )
}
