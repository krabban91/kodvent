import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "kodvent"
ThisBuild / organizationName := "kodvent"

lazy val root = (project in file("."))
  .settings(
    name := "aoc2023",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      combinators % Compile,
      kodventCommon % Compile,
      kodventScalaCommon % Compile,
    )
  )

resolvers += Resolver.mavenLocal
