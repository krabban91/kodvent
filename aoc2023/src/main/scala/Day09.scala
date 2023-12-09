import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = strings.map(parseInput).map(differences).map(extrapolate(_)).sum

  override def part2(strings: Seq[String]): Long = strings.map(parseInput).map(differences).map(extrapolate(_, part2 = true)).sum

  private def parseInput(input: String): Seq[Long] = {
    input.split(" ").map(_.toLong).toSeq
  }

  private def differences(input: Seq[Long]): Seq[Seq[Long]] = {
    val out = mutable.ListBuffer[Seq[Long]]()
    out.append(input)
    while (out.last.exists(_ != 0)) {
      out.append(out.last.sliding(2).map(t => t.last - t.head).toSeq)
    }
    out.toSeq
  }

  private def extrapolate(differences: Seq[Seq[Long]], part2: Boolean = false): Long = differences
    .map(l => if (part2) l.head else l.last)
    .reduceRight(_ + (if (part2) -1 else 1) * _)
}
