import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9"
  lazy val combinators = "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0"
  lazy val kodventCommon = "kodvent" % "common" % "0.0.1-SNAPSHOT"
  lazy val kodventScalaCommon = "kodvent" % "scala-common_2.13" % "0.1.0-SNAPSHOT"
}
