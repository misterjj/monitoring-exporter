package fr.jonathanjorand

import fr.jonathanjorand.YoutrackSpi.CountResponseFactory
import fr.jonathanjorand.config.YoutrackConfig
import scalaj.http._
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class YoutrackSpi(config: YoutrackConfig) {
  def getCount(query: String): Future[Int] = {
    val path = "api/issuesGetter/count?fields=count"

    Future {
      val response = {
        Http(config.host + "/" + path)
          .postData(s"""{"query":"$query"}""")
          .header("Authorization", "Bearer " + config.token)
          .header("content-type", "application/json")
          .asString
      }
      CountResponseFactory(response.body).count
    }
  }
}

object YoutrackSpi {
  case class CountResponse(
      $type: String,
      count: Int
  )

  object CountResponseFactory {
    def apply(string: String): CountResponse = {
      implicit val personFormat: RootJsonFormat[CountResponse] = jsonFormat2(CountResponse)
      string.parseJson.convertTo[CountResponse]
    }
  }

}
