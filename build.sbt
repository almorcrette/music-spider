ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

val circeVersion = "0.14.6"
val http4sVersion = "0.23.23"
val blazeServerVersion = "0.23.15"


val circeDependencies = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

val http4sDependencies = Seq(
  "org.http4s" %% "http4s-ember-client",
  "org.http4s" %% "http4s-dsl",
  "org.http4s" %% "http4s-circe"
).map(_ % http4sVersion)


val mainDependencies = Seq(
  "org.typelevel" %% "cats-effect" % "3.5.0",
  "com.beachape"  %% "enumeratum-play" % "1.7.3",
  "org.jsoup"     % "jsoup"            % "1.15.4"
) ++
  circeDependencies ++
  http4sDependencies

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0" % Test

)

lazy val root = (project in file("."))
  .settings(
    name := "music-spider",
    libraryDependencies ++= mainDependencies ++ testDependencies
  )
