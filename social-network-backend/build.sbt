name := """social-network"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
//libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"

//libraryDependencies += "com.h2database" % "h2" % "1.4.199"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.15"
libraryDependencies += specs2 % Test
libraryDependencies += filters
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
