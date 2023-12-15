import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    strings.head.split(",").map(HASH).toSeq.map(_.initialize).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val HASHMap = mutable.HashMap[Long, Seq[HASH]]()
    strings.head.split(",").map(HASH).toSeq.foreach { h =>
      val loc = h.label
      if (h.operation) {
        val curr = HASHMap.getOrElse(loc, Seq[HASH]())
        val i = curr.indexWhere(_.labelString == h.labelString)
        HASHMap.put(loc, if (i == -1) curr ++ Seq(h) else curr.take(i) ++ Seq(h) ++ curr.drop(i + 1))
      } else {
        HASHMap.put(loc, HASHMap.getOrElse(loc, Seq[HASH]()).filterNot(_.labelString == h.labelString))
      }
    }
    HASHMap.map { case (i, lenses) => lenses.zipWithIndex.map { case (h, ix) => (i + 1) * (ix + 1) * h.focalLength }.sum }.sum
  }

  case class HASH(str: String) {
    def initialize: Long = calculate(str)

    def label: Long = calculate(labelString)

    def labelString: String = str.split("=").head.split("-").head

    def operation: Boolean = str.contains("=")

    def focalLength: Long = if (str.contains("=")) str.split("=").last.toLong else 0L

    private def calculate(v: String): Long = v.foldLeft(0L) { case (o, h) => ((o + h.toLong) * 17) % 256 }
  }

}
