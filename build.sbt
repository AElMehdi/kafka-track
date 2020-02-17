import Dependencies._

ThisBuild / scalaVersion     := "2.12.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "kafkaTrack",
    libraryDependencies += scalaTest % Test
  )

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"
libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.4.0"
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.30"
