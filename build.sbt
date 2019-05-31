name         := "play-routing-case-study"
organization := "com.example"
version      := "1.0-SNAPSHOT"
scalaVersion := "2.12.8"

enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test,
)

routesImport ++= Seq(
  "java.time.LocalDate",
)
