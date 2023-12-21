package apps

import cats.effect.{ExitCode, IO, IOApp}
import connectors.WikipediaReader
import support.HttpClient

object Application extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val name = args.head
    
    val wikipediaReader: WikipediaReader = new WikipediaReader(new HttpClient)
    
    wikipediaReader
      .getWikipediaPageFor(name)
      .map(println)
      .as(ExitCode.Success)
    
  }

}
