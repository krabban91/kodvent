import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "kodvent"
ThisBuild / organizationName := "kodvent"

lazy val root = (project in file("."))
  .settings(
    name := "aoc2025",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      combinators % Compile,
      //kodventCommon % Compile,
      kodventScalaCommon % Compile,
      "org.scala-lang.modules" %% "scala-parallel-collections" % "1.2.0",
    )
  )

resolvers += Resolver.mavenLocal
