package connectors

import cats.effect.IO
import model.{BandInfo, Genre}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

import java.time.LocalDate
import scala.jdk.CollectionConverters.*


class WikipediaReader {

  def getInfoForBand(name: String): IO[BandInfo] = getDoc(name) map { doc =>
    val infoBoxRows = doc.select(".infobox.vcard.plainlist").select("tr").asScala.toList
    BandInfo(name, getGenresFromInfo(infoBoxRows), getYearsActiveFromInfo(infoBoxRows))

  } handleErrorWith { (ex: Throwable) =>
    IO.raiseError(new RuntimeException(s"Could not parse info from Wikipedia for $name - $ex"))
  }
  
  def getAllLinksForPage(name: String) = getDoc(name) map { doc =>
    doc.select("a")
  }

  private def getGenresFromInfo(infoBoxRows: List[Element]): List[Genre] =
    infoBoxRows(4).select("li").asScala.toList.map { el =>
      Genre(
        name = el.text(),
        url = s"https://en.wikipedia.org${el.select("a").asScala.map(_.attr("href")).mkString}"
      )
    }

  private def getYearsActiveFromInfo(infoBoxRows: List[Element]): List[Int] =
    infoBoxRows(5).select("li").asScala.toList.flatMap { el =>
      parseYearsActive(trimYearsActiveElement(el))
    }


  private def getDoc(name: String): IO[Document] = IO {
    Jsoup.connect(wikipediaUrl(name)).get()
  } handleErrorWith { (ex: Throwable) =>
    IO.raiseError(new RuntimeException(s"Could not fetch Wikipedia article for $name - $ex"))
  }

  private def wikipediaUrl(name: String): String = {
    s"https://en.wikipedia.org/wiki/${name.replace(' ', '_')}"
  }

  private def trimYearsActiveElement(el: Element): String = {
  el.toString.stripPrefix("<li>").stripPrefix("<span class=\"nowrap\">").stripSuffix("</li>").stripSuffix("</span>")
  }

  private def parseYearsActive(yearsActiveString: String): List[Int] = {
    if (!yearsActiveString.contains('–')) List(yearsActiveString.toInt)
    else if yearsActiveString.contains("–present") then {
      (yearsActiveString.stripSuffix("–present").toInt to LocalDate.now().getYear).toList
    } else {
      val yearsActiveStartEnd = yearsActiveString.split('–').map(_.toInt)
      (yearsActiveStartEnd(0) to yearsActiveStartEnd(1)).toList
    }
  }

}
