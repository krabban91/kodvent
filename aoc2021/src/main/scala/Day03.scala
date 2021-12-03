import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable.ListBuffer
import scala.util.chaining.scalaUtilChainingOps

object Day03 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val input = strings.map(s => s.toCharArray)

    val out = input.foldLeft(input.head.indices.map(_ => (0, 0)))((res, in) => {
      val v = ListBuffer[(Int, Int)]()
      for (i <- res.indices) {
        v.append((res(i)._1 + (if (in(i) == '1') 1 else 0), res(i)._2 + (if (in(i) == '0') 1 else 0)))
      }
      v.toSeq.toIndexedSeq
    })
    val gamma = out.map(t => if (t._1 > t._2) "1" else "0").reduce((a, b) => a + b).pipe(Integer.parseInt(_, 2))
    val epsilon = out.map(t => if (t._1 < t._2) "1" else "0").reduce((a, b) => a + b).pipe(Integer.parseInt(_, 2))
    gamma * epsilon

  }

  override def part2(strings: Seq[String]): Long = -1
}
