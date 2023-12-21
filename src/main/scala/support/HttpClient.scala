package support

import cats.effect.{IO, Resource}
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder

class HttpClient {

  implicit lazy val HttpClientResource: Resource[IO, Client[IO]] = EmberClientBuilder
    .default[IO]
    .build

  private def useClient(call: Client[IO] => IO[String])
                       (implicit clientResource: Resource[IO, Client[IO]]): IO[String] = {
    clientResource.use(call)
  }

  def get(url: String): IO[String] = useClient { client =>
    client.expect[String](url)}
}
