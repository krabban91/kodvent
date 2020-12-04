package aoc.input

import scala.io.Source

trait Inputs {

  def getInput: Seq[String] = read(s"${this.getClass.getName.toLowerCase}.txt")

  def read(path: String): Seq[String] = {
    val source = Source.fromResource(path)
    try {
      source.getLines().toSeq
    } finally {
      source.close()
    }
  }
}
