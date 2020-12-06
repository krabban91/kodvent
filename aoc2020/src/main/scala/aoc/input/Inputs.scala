package aoc.input

import scala.collection.mutable
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

  def groupsSeparatedByTwoNewlines(strings: Seq[String]): Seq[String] = {
    var groups = mutable.ListBuffer[String]()
    val sb = new mutable.StringBuilder
    for (string <- strings) {
      if (string.isBlank) {
        groups += sb.toString()
        sb.clear()
      } else {
        sb.append(s"\n${string}")
      }
    }
    groups += sb.toString()
    groups.toSeq
  }
}
