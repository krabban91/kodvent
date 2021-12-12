import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = generatePaths(graph(strings), part2 = false).size

  override def part2(strings: Seq[String]): Long = generatePaths(graph(strings), part2 = true).size

  def graph(strings: Seq[String]): Map[String, Seq[String]] = strings
    .map(s => s.split(("-")))
    .flatMap(l => Seq((l.head, l.last), (l.last, l.head)))
    .groupBy(_._1)
    .map(t => (t._1, t._2.map(_._2).filterNot(_ == "start")))


  def generatePaths(graph: Map[String, Seq[String]], part2: Boolean): Set[Seq[String]] = {
    val smallCaves = graph.keys.filter(_ (0).isLower).toSet
    val paths = mutable.HashSet[Seq[String]]()
    val frontier = mutable.Stack[Seq[String]]()
    frontier.push(Seq("start"))
    val checked = mutable.HashSet[Seq[String]]()

    while (frontier.nonEmpty) {
      Option(frontier.pop())
        .filterNot(checked.contains)
        .foreach(curr => {
          checked.add(curr)
          graph(curr.last)
            .filter(s => !smallCaves.contains(s) || !curr.contains(s) || (part2 && !curr.filter(smallCaves.contains).groupBy(v => v).values.exists(_.size > 1)))
            .map(s => curr ++ Seq(s))
            .filterNot(checked.contains)
            .foreach {
              case p if p.last == "end" => paths.add(p)
              case p => frontier.push(p)
            }
        })
    }
    paths.toSet
  }
}
