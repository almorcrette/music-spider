package connectors

import cats.effect.IO
import support.HttpClient

class WikipediaReader(httpClient: HttpClient) {
  def getWikipediaPageFor(name: String): IO[String] = {
    httpClient
      .get(wikipediaUrl(name))
      .handleErrorWith {
        (ex: Throwable) => IO.raiseError(new RuntimeException(s"Error fetching wikipedia page = $ex"))
      }
  }
  
  private def wikipediaUrl(name: String): String = {
    s"https://en.wikipedia.org/wiki/$name"
  }

}
