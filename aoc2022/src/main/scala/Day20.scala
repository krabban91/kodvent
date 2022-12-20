import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day20 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(v => v.toInt)
    val l = mixNumbers(v)
    val i0 = l.indexOf(0)
    val i1000 = (i0 + 1000)
    val i2000 = (i0 + 2000)
    val i3000 = (i0 + 3000)

    val i = l(i1000 % v.size)
    val i1 = l(i2000 % v.size)
    val i2 = l(i3000 % v.size)
    i + i1 + i2
    // -27706 is wrong
    // 5195 is wrong
  }

  private def mixNumbers(v: Seq[Int]): mutable.ArrayDeque[Int] = {

    val l = mutable.ArrayDeque[(Int, Int)]()
    val initial =v.zipWithIndex
    l.addAll(initial)
    initial.foreach(x => {
      // move to item
      while (l.head != x) {
        l.append(l.removeFirst(_ => true).get)
      }
      //move item
      val o = l.removeFirst(_ => true).get
      if (x != o) {
        println(s"moved wrong item x=${x}, o=$o")
      }
      val times = x._1
      if (times > 0) {
        (1 to times).foreach(t => l.append(l.removeFirst(_ => true).get))
        l.prepend(x)
        //move back
        (1 to (times)).foreach(t => l.prepend(l.removeLast()))
      } else {
        (1 to math.abs(times)).foreach(t => l.prepend(l.removeLast()))
        l.prepend(x)
        //move back
        (1 to math.abs(times)).foreach(t => l.append(l.removeFirst(_ => true).get))

      }

    })
    l.append(l.removeFirst(_ => true).get)
    l.map(_._1)
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
