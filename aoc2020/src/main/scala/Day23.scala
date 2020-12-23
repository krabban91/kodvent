import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2


  override def part1(strings: Seq[String]): Long = {
    val inp: Seq[Int] = strings.head.map(c => Integer.parseInt(c.toString))
    var cups = mutable.HashMap() ++ inp.indices.map(i=> inp(i) -> inp((i+1)%inp.size)).toMap
    var current = inp.head
    val moves = 100
    val maxV = cups.values.max
    val minV = cups.values.min
    for (move <- Range(0, moves)) {
      val res = moveCups(cups, current, move, minV, maxV)
      cups = res._1
      current = res._2
    }
    val sb = new StringBuilder()
    var i = cups(1)
    while(i != 1){
      sb.append(i)
      i = cups(i)
    }
    sb.toString().toLong
  }

  override def part2(strings: Seq[String]): Long = {
    val inp: Seq[Int] = strings.head.map(c => Integer.parseInt(c.toString)) ++ Range(strings.head.length+1,1_000_000+1)
    var cups = mutable.HashMap() ++ inp.indices.map(i=> inp(i) -> inp((i+1)%inp.size)).toMap
    var current = inp.head
    val moves = 10_000_000
    val maxV = cups.values.max
    val minV = cups.values.min
    for (move <- Range(0, moves)) {
      val res = moveCups(cups, current, move, minV, maxV)
      cups = res._1
      current = res._2
    }
    cups(1).toLong * cups(cups(1))
  }

  def moveCups(cups: mutable.HashMap[Int, Int], current: Int, move: Int, minV: Int, maxV: Int): (mutable.HashMap[Int, Int], Int) = {
    val debug = false

    val pickedup = Seq(cups(current), cups(cups(current)), cups(cups(cups(current))))
    cups.update(current, cups(cups(cups(cups(current)))))
    var destination = current -1
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

    (cups, cups(current))
  }
}
