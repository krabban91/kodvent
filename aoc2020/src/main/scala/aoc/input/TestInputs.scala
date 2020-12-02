package aoc.input

import scala.io.Source

trait TestInputs {

  def getInputTest: Seq[String] = {
    val source = Source.fromResource(s"test/${this.getClass.getName.toLowerCase}.txt")
    try {
      source.getLines().toSeq
    } finally {
      source.close()
    }
  }
}
