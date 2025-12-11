import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day11 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2


  case class Device(id: String, out: Seq[String])

  object Device {
    def apply(str: String) : Device = {
      val v = str.split(":")
      val id = v.head.strip()
      val to = v.last.strip().split(" ").toSeq
      Device(id, to)
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val devices = (strings.map(Device(_)) :+ Device("out", Seq())).map(d => (d.id, d)).toMap
    val pathsTo =  mutable.HashMap[String, Long]()
    val visitedPaths = mutable.HashSet[Seq[String]]()
    val toExplore = mutable.PriorityQueue[Seq[String]]()(Ordering.by(-_.size))
    toExplore.addOne(Seq("you"))
    while(toExplore.nonEmpty) {
      val pop = toExplore.dequeue()
      if (!visitedPaths(pop)){
        visitedPaths.add(pop)
        val current = pop.last
        pathsTo.update(current, pathsTo.getOrElse(current, 0L) + 1)
        val next = devices(current).out.map(n => pop :+ n)
          .filterNot(visitedPaths)
        toExplore.addAll(next)
      }
    }
    pathsTo("out")
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
