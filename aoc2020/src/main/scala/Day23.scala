import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (cups, current) = getCups(strings, strings.head.length)
    val moves = 100
    moveCupsNTimes(cups, current, moves)
    val sb = new StringBuilder()
    var i = cups(1)
    while (i != 1) {
      sb.append(i)
      i = cups(i)
    }
    sb.toString().toLong
  }

  override def part2(strings: Seq[String]): Long = {
    val (cups, current) = getCups(strings, 1_000_000)
    val moves = 10_000_000
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
    val debug = false

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

    if (debug) {
      println(s"-- move ${move + 1} --")
      println(s"cups: $cups")
      println(s"current: $current")
      println(s"pick up: $pickedup")
      println(s"destination: $destination")
    }

    cups(current)
  }
}