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
    val tuple = Graph.longestPath[(Long, Long)](Seq(start), p => p == end, _ manhattan end, p => {
      val neighbors = map(p) match {
        case "." => DIRECTIONS.map(_ + p)
        case ">" => Seq(EAST + p)
        case "<" => Seq(WEST + p)
        case "v" => Seq(SOUTH + p)
        case "^" => Seq(NORTH + p)
        case _ => Seq()
      }
      val value = neighbors.filter(n => map.get(n).exists(s => s != "#"))
      value.map((_, 1L))
    })
    tuple

  }

  def distances(map: Map[(Long, Long), String], start: (Long, Long), end: (Long, Long)): Map[(Long, Long), Seq[((Long, Long), Long)]] = {
    // type Point = (Long, Long)

    val crossings: Map[(Long, Long), String] = getCrossings(map, start)


    val neighbors = (p: (Long, Long)) => DIRECTIONS.map(d => d + p).filter(n => map.get(n).exists(s => s != "#")).map((_, 1))

    val toExplore = mutable.Queue[((Long, Long), (Long, Long))]()
    val distances = mutable.HashMap[Set[(Long, Long)], Long]()
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
          val path = Set(loc, pos)
          distances.put(path, cost)
        } else if (been.add(pos)) {
          neighbors(pos)
            .filterNot(t => been.contains(t._1))
            .foreach { p =>
              val path = Set(loc, p._1)
              if (crossings.contains(p._1)) {
                if (!distances.contains(path) || distances(path) < cost) {
                  distances.put(path, cost + p._2)
                  neighbors(p._1).map(_._1).filter(_ != pos).map(n => (p._1, n - p._1)).foreach(n => toExplore.addOne(n))
                }
                // 3, 4 n
                // 3, 5 p._1
                // n - p._1 = 0, -1 (NORTH)

              } else {
                queue.enqueue((p._1, cost + p._2))
              }
            }
        }
      }
    }
    val value1 = distances.toSeq.flatMap(v => v._1)
    val value: Map[(Long, Long), Seq[((Long, Long), Seq[((Long, Long), Long)])]] = value1.map { p =>
      val value3 = distances
        .filter(v => v._1.contains(p))
        .toSeq.map(kv => (kv._1.find(v => v != p), kv._2))
      val value2 = value3.filter(_._1.isDefined).map(kv => (kv._1.get, kv._2))
      (p, value2)
    }
      .groupBy(_._1)
    val value2 = value
      .map(kv => (kv._1, kv._2.flatMap(_._2).distinct))
    value2
  }

  def getCrossings(map: Map[(Long, Long), String], start: (Long, Long)) = {
    map
      .filterNot(_._2 == "#")
      .filter { case (p, _) => DIRECTIONS.map(_ + p).count(n => map.get(n).exists(_ != "#")) >= 3 } ++ Map(start -> ".")
  }

  override def part2(strings: Seq[String]): Long = {
    val (map, start, end) = parse(strings)

    val ds = distances(map, start, end)
    val tuple = Graph.longestPath[(Long, Long)](Seq(start), p => p == end, _ manhattan end, p => {
      ds(p)
    })

  /*
    val tuple = Graph.longestPath[(Long, Long)](Seq(start), p => p == end, _ manhattan end, p => {
      val neighbors = map(p) match {
        case "." | ">" | "<" | "v" | "^" => DIRECTIONS.map(_ + p)
        case _ => Seq()
      }
      val value = neighbors.filter(n => map.get(n).exists(s => s != "#"))
      value.map((_, 1L))
    })
    */
    // 5966 is too low
    // 6058 is not the right answer
    tuple
  }

  def parse(strings: Seq[String]): (Map[(Long, Long), String], (Long, Long), (Long, Long)) = {
    val map = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), s"$c") } }.toMap
    val start = (strings.head.indexOf(".").toLong, 0L)
    val end = (strings.last.indexOf(".").toLong, strings.size.toLong - 1)

    (map, start, end)
  }

}
