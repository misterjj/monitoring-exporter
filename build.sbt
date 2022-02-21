lazy val akkaHttpVersion = "10.2.7"
lazy val akkaVersion = "2.6.18"

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "fr.jonathanjorand",
      scalaVersion := "2.13.4"
    )
  ),
  name := "akka",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "org.scalaj" %% "scalaj-http" % "2.4.2",
    "io.spray" %% "spray-json" % "1.3.6",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.typelevel" %% "cats-core" % "2.6.1",
    "org.typelevel" %% "cats-kernel" % "2.6.1"
  )
)
