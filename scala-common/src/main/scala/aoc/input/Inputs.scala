package aoc.input

import java.io.FileNotFoundException
import scala.collection.mutable
import scala.io.{BufferedSource, Source}

trait Inputs {

  def getInput: Seq[String] = read(s"${this.getClass.getName.toLowerCase}.txt")

  def read(path: String): Seq[String] = {
    val source: BufferedSource = try {
      Source.fromResource(path)
    }
    catch {
      case _: FileNotFoundException =>
        throw new RuntimeException("Input not retrieved from AOC. run './read.sh <day> first'")
    }
    try {
      val lines = source.getLines().toSeq
      if (lines.head.startsWith("====WAITING FOR INPUT====")) {
        throw new RuntimeException("Input not retrieved from AOC. run './read.sh <day> first'")
      }
      lines
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
