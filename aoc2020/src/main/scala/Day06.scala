import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable


object Day06 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val in = fromRaw(strings)
    val s = in.map(_.replace("\t", "")).map(s => s.toSeq.distinct.size)
    s.sum
  }

  override def part2(strings: Seq[String]): Long = {
    val in = fromRaw(strings)
    val s = in.map(_.split("\t").filterNot(_.isBlank)).map(s => {
      val m = mutable.Map[Int, Int]()
      for (ss <- s) {
        ss.chars.forEach(i => m(i) = 1 + m.getOrElse(i, 0))
      }
      m.values.count(_ == s.length)
    })
    s.sum
  }

  def fromRaw(strings: Seq[String]): Seq[String] = {
    var groups = mutable.ListBuffer[String]()
    val sb = new mutable.StringBuilder
    for (string <- strings) {
      if (string.isBlank) {
        groups += sb.toString()
        sb.clear()
      } else {
        sb.append(s"\t${string}")
      }
    }
    groups += sb.toString()
    groups.toSeq
  }
}
