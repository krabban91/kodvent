import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable


object Day06 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = fromRaw(strings)
    .map(_.replace("\t", ""))
    .map(_.toSeq.distinct.size)
    .sum

  override def part2(strings: Seq[String]): Long = fromRaw(strings)
    .map(_.split("\t").filterNot(_.isBlank))
    .map(respondents => respondents
      .flatMap(_.toSeq).toSeq.distinct
      .map(q => respondents.count(_.contains(q)))
      .count(_ == respondents.length))
    .sum

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
