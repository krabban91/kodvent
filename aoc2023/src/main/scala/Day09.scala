import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  def differences(input: Seq[Seq[Long]]): Seq[Seq[Seq[Long]]] = {
    input.map { l =>
      val out = mutable.ListBuffer[Seq[Long]]()
      out.append(l)
      while (out.last.exists(_ != 0)) {
        val value = out.last.sliding(2).map(t => t.last - t.head).toSeq
        out.append(value)
      }
      out.toSeq
    }
  }

  def extrapolate(input: Seq[Seq[Seq[Long]]]): Seq[Long] = {
    input.map{ls =>
      ls.foldRight(0L){ case (l, v) =>
        l.last + v
      }
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val input = strings.map(_.split(" ").map(_.toLong).toSeq)
    val calculated = differences(input)
    val extrapolated = extrapolate(calculated)
    extrapolated.sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
