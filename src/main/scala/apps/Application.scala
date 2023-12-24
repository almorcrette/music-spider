package apps

import cats.effect.{ExitCode, IO, IOApp}
import connectors.WikipediaReader

object Application extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val name = args.head
    
    val wikipediaReader: WikipediaReader = new WikipediaReader
    
    wikipediaReader
      .getInfoForBand(name)
      .map(println)
      .as(ExitCode.Success)
    
  }

}

object Sandbox extends IOApp.Simple {
  override def run: IO[Unit] = {
    val wikipediaReader: WikipediaReader = new WikipediaReader

    wikipediaReader
      .getAllLinksForPage("Alexisonfire")
      .map(println)
  }
}
