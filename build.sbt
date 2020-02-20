
ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.aelmehdi"

lazy val kafkaTrack = (project in file("."))
      .settings(
         name := "kafkaTrack",
         libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0",
         libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.4.0",
         libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.0",
         libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test",
         libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.30",
      )
