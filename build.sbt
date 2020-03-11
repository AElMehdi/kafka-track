
ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.aelmehdi"

//addSbtPlugin("com.tapad" % "sbt-docker-compose" % "1.0.35")
//
////enablePlugins(DockerPlugin, DockerComposePlugin)

//enablePlugins(DockerComposePlugin)
//dockerImageCreationTask := docker.value

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.3")

lazy val kafkaTrack = (project in file("."))
  .settings(
    name := "kafkaTrack",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.3",
    libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0",
    libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.4.0",
    libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.30",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test,
    libraryDependencies += "com.dimafeng" %% "testcontainers-scala-kafka" % "0.35.2" % Test,
    libraryDependencies += "org.apache.kafka" % "kafka-streams-test-utils" % "2.4.0" % Test,
    libraryDependencies += "org.assertj" % "assertj-core" % "3.15.0" % Test
  )
