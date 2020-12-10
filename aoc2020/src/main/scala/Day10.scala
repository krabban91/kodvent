import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day10 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val in = strings.map(_.toLong).sorted
    val diffs = mutable.Map[Long, Long](3L -> 1)
    var current = 0L
    for (adapter <- in) {
      val diff = adapter - current
      diffs.updateWith(diff)(l => l.map(_ + 1).orElse(Option(1)))
      current = adapter
    }
    diffs(1) * diffs(3)
  }

  override def part2(strings: Seq[String]): Long = {
    val in = strings.map(_.toLong).sorted.reverse
    val permutations = mutable.Map[Long, Long](in.max -> 1).withDefaultValue(0)
    for (adapter <- in) {
      val valid = permutations.toSeq.filter(l => Seq(1, 2, 3).contains(l._1 - adapter))
      valid.foreach(t => permutations.updateWith(adapter)(l => l.map(_ + t._2).orElse(Option(t._2))))
    }
    permutations(1) + permutations(2) + permutations(3)
  }
}
