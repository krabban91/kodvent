import Dependencies._

ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "kodvent"
ThisBuild / organizationName := "kodvent"

lazy val root = (project in file("."))
  .settings(
    name := "aoc2017",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      kodventCommon % Compile,
      kodventScalaCommon % Compile
    )
  )
resolvers += Resolver.mavenLocal
