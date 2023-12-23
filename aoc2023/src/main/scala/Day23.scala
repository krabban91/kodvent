import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

import scala.collection.mutable

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (map, start, end) = parse(strings)
    val ds = distances(map, start, end, neighborFunction(map, part2 = false))
    Graph.longestPath[(Long, Long)](Seq(start), _ == end, _ manhattan end, ds.getOrElse(_, Seq()))._1
  }

  override def part2(strings: Seq[String]): Long = {
    val (map, start, end) = parse(strings)
    val ds = distances(map, start, end, neighborFunction(map, part2 = true))
    Graph.longestPath[(Long, Long)](Seq(start), _ == end, _ manhattan end, ds.getOrElse(_, Seq()))._1
  }

  def parse(strings: Seq[String]): (Map[(Long, Long), String], (Long, Long), (Long, Long)) = {
    val map = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), s"$c") } }.toMap
    val start = (strings.head.indexOf(".").toLong, 0L)
    val end = (strings.last.indexOf(".").toLong, strings.size.toLong - 1)
    (map, start, end)
  }

  def neighborFunction(map: Map[(Long, Long), String], part2: Boolean = false): ((Long, Long)) => Seq[((Long, Long), Long)] = p => {
    val n = if (part2) {
      map(p) match {
        case "." | ">" | "<" | "v" | "^" => DIRECTIONS.map(_ + p)
        case _ => Seq()
      }
    } else {
      map(p) match {
        case "." => DIRECTIONS.map(_ + p)
        case ">" => Seq(EAST + p)
        case "<" => Seq(WEST + p)
        case "v" => Seq(SOUTH + p)
        case "^" => Seq(NORTH + p)
        case _ => Seq()
      }
    }
    n.filter(n => map.get(n).exists(s => s != "#")).map((_, 1L))
  }

  def getCrossings(map: Map[(Long, Long), String], start: (Long, Long)) = map
    .filterNot(_._2 == "#")
    .filter { case (p, _) => DIRECTIONS.map(_ + p).count(n => map.get(n).exists(_ != "#")) >= 3 } ++ Map(start -> ".")


  def distances(map: Map[(Long, Long), String], start: (Long, Long), end: (Long, Long), neighbors: ((Long, Long)) => Seq[((Long, Long), Long)]): Map[(Long, Long), Seq[((Long, Long), Long)]] = {
    val crossings: Map[(Long, Long), String] = getCrossings(map, start)

    val toExplore = mutable.Queue[((Long, Long), (Long, Long))]()
    val distances = mutable.HashMap[Seq[(Long, Long)], Long]()
    toExplore.addOne((start, SOUTH))
    while (toExplore.nonEmpty) {
      val (loc, dir) = toExplore.dequeue()
      val queue = mutable.PriorityQueue[((Long, Long), Long)]()(Ordering.by(-_._2))
      val been = mutable.HashSet[(Long, Long)]()
      been.add(loc)
      queue.addOne((loc + dir, 1L))
      while (queue.nonEmpty) {
        val (pos, cost) = queue.dequeue()
        if (pos == end) {
          val path = Seq(loc, pos)
          distances.put(path, cost)
        } else if (crossings.contains(pos)) {
          val path = Seq(loc, pos)
          if (!distances.contains(path) || distances(path) < cost) {
            distances.put(path, cost)
            neighbors(pos).map(_._1).map(n => (pos, n - pos)).foreach(n => toExplore.addOne(n))
          }
        }
        else if (been.add(pos)) {
          neighbors(pos)
            .filterNot(t => been.contains(t._1))
            .foreach { p => queue.enqueue((p._1, cost + p._2)) }
        }
      }
    }

    distances.toSeq.map(v => (v._1.head, (v._1.last, v._2)))
      .filterNot(v => v._1 == v._2._1)
      .groupBy(_._1)
      .map(kv => (kv._1, kv._2.map(_._2)))
  }

}
