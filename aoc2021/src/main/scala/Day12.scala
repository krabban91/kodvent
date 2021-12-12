import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val inp = strings.map(s=>s.split(("-"))).flatMap(l => Seq((l.head, l.last), (l.last, l.head)))
    val m = inp.groupBy(_._1)
    val uniquePaths = mutable.HashSet[Seq[String]]()
    uniquePaths.add(Seq("start"))
    val frontier = mutable.Stack[String]()

    frontier.push("start")
    while (frontier.nonEmpty){
      val curr = frontier.pop()
      val doors = m.getOrElse(curr, Seq())
      val paths: Seq[Seq[String]] = uniquePaths.filter(_.last==curr).toSeq
      val newPaths: Seq[Seq[String]] = paths.flatMap(p => {
        doors.map(_._2)
          .filter(s=> s(0).isUpper || !p.contains(s))
          .map(s => {
            val l = p ++ Seq(s)
            if(s != "end" && !uniquePaths.contains(l)){
              frontier.push(s)
            }
            l
          })
      })
      uniquePaths.addAll(newPaths)
    }
    val ends = uniquePaths.filter(_.last == "end")
    ends.size
  }

  override def part2(strings: Seq[String]): Long = -1
}
