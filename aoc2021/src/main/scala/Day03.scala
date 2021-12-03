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

    val counts = countOccurrences(input)
    val gamma = counts.map(t => if (t._1 > t._2) '1' else '0').foldLeft("")((a, b) => a + b).pipe(Integer.parseInt(_, 2))
    val epsilon = counts.map(t => if (t._1 < t._2) '1' else '0').foldLeft("")((a, b) => a + b).pipe(Integer.parseInt(_, 2))
    gamma * epsilon

  }

  override def part2(strings: Seq[String]): Long = {
    val input = strings.map(s => s.toCharArray)
    val o2Rating = input.head.indices.foldLeft(input)((oInput, a) => ratingsReducer(oInput, a, isO2 = true))
      .head.foldLeft("")((l, r) => l + r).pipe(Integer.parseInt(_, 2))
    val co2Rating = input.head.indices.foldLeft(input)((oInput, a) => ratingsReducer(oInput, a, isO2 = false))
      .head.foldLeft("")((l, r) => l + r).pipe(Integer.parseInt(_, 2))
    o2Rating * co2Rating
  }

  private def ratingsReducer(input: Seq[Array[Char]], a: Int, isO2: Boolean): Seq[Array[Char]] = {
    if (input.size == 1) input else {
      val counts = countOccurrences(input)
      input.filter(s => s(a) == (if (isO2) '0' else '1') == counts(a)._1 < counts(a)._2)
    }
  }

  private def countOccurrences(input: Seq[Array[Char]]): Seq[(Int,Int)] = {
    input.foldLeft(input.head.indices.toList.map(_ => (0, 0)))((res, in) => {
      val v = ListBuffer[(Int, Int)]()
      for (i <- res.indices) {
        v.append((res(i)._1 + in(i).asDigit, res(i)._2 + math.abs(in(i).asDigit - 1)))
      }
      v.toList
    })
  }
}
