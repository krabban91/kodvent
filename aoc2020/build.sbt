import Dependencies._

ThisBuild / scalaVersion := "2.13.12"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "kodvent"
ThisBuild / organizationName := "kodvent"

lazy val root = (project in file("."))
  .settings(
    name := "aoc2020",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      kodventCommon % Compile,
      kodventScalaCommon % Compile
    )
  )
resolvers += Resolver.mavenLocal

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
