import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day05 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (ranges, ingredients) = parseInput(strings)
    val fresh = ingredients.filter { ing =>
      ranges.exists(withinSpan(ing, _))
    }
    fresh.size
  }

  override def part2(strings: Seq[String]): Long = {
    val (ranges, _) = parseInput(strings)
    val joined = mergedRanges(ranges)
    joined.map { case (s, e) => e - s + 1 }.sum
  }

  private def parseInput(strings: Seq[String]): (Seq[(Long, Long)], Seq[Long]) = {
    val sep = strings.indexOf("")
    val ranges = strings.take(sep).map(s => s.split("-") match {
      case Array(start, end) => (start.toLong, end.toLong)
    })
    val ingredients = strings.drop(sep + 1).map(_.toLong)
    (ranges, ingredients)
  }

  private def withinSpan(value: Long, range: (Long, Long)): Boolean = value >= range._1 && value <= range._2

  private def overlaps(range1: (Long, Long), range2: (Long, Long)): Boolean = {
    val (c1, c2) = range1
    withinSpan(c1, range2) || withinSpan(c2, range2)
  }

  private def mergedRanges(ranges: Seq[(Long, Long)]): Seq[(Long, Long)] = {
    val joined = mutable.HashSet[(Long, Long)]()
    joined.addAll(ranges)
    ranges.foreach{ r0 =>
      val overlapping = joined
        .filter(overlaps(r0, _)).toSeq
      if (overlapping.size > 1) {
        overlapping.foreach(r => joined.remove(r))
        val curr = conjoint(overlapping)
        joined.add(curr)
      }
    }
    joined.toSeq
  }

  private def conjoint(ranges: Seq[(Long, Long)]): (Long, Long) = {
    var curr = (Long.MaxValue, Long.MinValue)
    ranges.foreach { case (l, r) => curr = (Math.min(curr._1, l), Math.max(curr._2, r)) }
    curr
  }
}
