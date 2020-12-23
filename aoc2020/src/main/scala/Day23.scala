import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val N = strings.head.length
    val moves = 100
    val (cups, current) = getCups(strings, N)
    moveCupsNTimes(cups, current, moves)
    Range(1, strings.head.length).foldLeft(("", cups(1)))((t, _) => (s"${t._1}${t._2}", cups(t._2)))._1.toLong
  }

  override def part2(strings: Seq[String]): Long = {
    val N = 1_000_000
    val moves = 10_000_000
    val (cups, current) = getCups(strings, N)
    moveCupsNTimes(cups, current, moves)
    cups(1).toLong * cups(cups(1))
  }

  def getCups(strings: Seq[String], size: Int): (mutable.HashMap[Int, Int], Int) = {
    val inp: Seq[Int] = strings.head.map(c => Integer.parseInt(c.toString)) ++ Range(strings.head.length + 1, size + 1)
    (mutable.HashMap() ++ inp.indices.map(i => inp(i) -> inp((i + 1) % inp.size)).toMap, inp.head)
  }

  def moveCupsNTimes(cups: mutable.HashMap[Int, Int], current: Int, moves: Int): Unit = {
    var curr = current
    val maxV = cups.values.max
    val minV = cups.values.min
    for (move <- Range(0, moves)) {
      curr = moveCups(cups, curr, move, minV, maxV)
    }
  }

  def moveCups(cups: mutable.HashMap[Int, Int], current: Int, move: Int, minV: Int, maxV: Int): Int = {
    val pickedup = Seq(cups(current), cups(cups(current)), cups(cups(cups(current))))
    cups.update(current, cups(cups(cups(cups(current)))))
    var destination = current - 1
    while (pickedup.contains(destination) || destination < minV) {
      destination -= 1
      if (destination < minV) {
        destination = maxV
      }
    }
    cups.update(pickedup(2), cups(destination))
    cups.update(destination, pickedup(0))
    cups(current)
  }
}
