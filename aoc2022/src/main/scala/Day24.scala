import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day24 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (walls, directions, bz, start, end) = extractMap(strings)
    shortestPath((start, 0), end, bz, directions, walls)._2
  }

  override def part2(strings: Seq[String]): Long = {
    val (walls, directions, bz, start, end) = extractMap(strings)
    val firstRun = shortestPath((start, 0), end, bz, directions, walls)._2
    val secondRun = shortestPath((end, firstRun), start, bz, directions, walls)._2
    val thirdRun = shortestPath((start, secondRun), end, bz, directions, walls)._2
    thirdRun
  }

  private def extractMap(strings: Seq[String]): (Set[(Int, Int)], Map[Char, (Int, Int)], Map[Int, Set[(Int, Int)]], (Int, Int), (Int, Int)) = {
    val v = strings.zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x, y), c) } }.toMap
    val directions = Map(
      '>' -> (1, 0),
      'v' -> (0, 1),
      '<' -> (-1, 0),
      '^' -> (0, -1))
    val blizzards0: Seq[((Int, Int), Char)] = v.filter(kv => directions.contains(kv._2)).toSeq
    val bz = (0 to 1000).foldLeft((blizzards0, Map[Int, Set[(Int, Int)]]())) { case ((curr, allPos), i) =>
      val next = nextBlizzardsMap(curr, v, directions)
      val curLocs = Map(i -> curr.map(_._1).toSet)
      (next, allPos ++ curLocs)
    }
    val minY = v.keySet.map(_._2).min
    val maxY = v.keySet.map(_._2).max

    val start = v.find(kv => kv._1._2 == minY && kv._2 == '.').map(_._1).get
    val end = v.find(kv => kv._1._2 == maxY && kv._2 == '.').map(_._1).get
    val walls = v.filter(_._2 == '#').keySet ++ Set((start._1, start._2 - 1), (end._1, end._2 + 1))
    (walls, directions, bz._2, start, end)
  }

  private def nextBlizzardsMap(blizzard: Seq[((Int, Int), Char)], v: Map[(Int, Int), Char], directions: Map[Char, (Int, Int)]): Seq[((Int, Int), Char)] = {
    val minX = v.keySet.map(_._1).min
    val maxX = v.keySet.map(_._1).max
    val minY = v.keySet.map(_._2).min
    val maxY = v.keySet.map(_._2).max
    blizzard.map { case ((x, y), c) =>
      val (dx, dy) = directions(c)
      val possible@(px, py) = (x + dx, y + dy)
      val pos = if (v(possible) == '#') {
        c match {
          case '>' => (minX + 1, py)
          case 'v' => (px, minY + 1)
          case '<' => (maxX - 1, py)
          case '^' => (px, maxY - 1)
        }
      } else {
        possible
      }
      (pos, c)
    }
  }

  private def heuristic(start: (Int, Int), end: (Int, Int)): Int = math.abs(start._1 - end._1) + math.abs(start._2 - end._2)


  private def shortestPath(start: ((Int, Int), Int), end: (Int, Int), blizzards: Map[Int, Set[(Int, Int)]], directions: Map[Char, (Int, Int)], walls: Set[(Int, Int)]) = {
    val frontier = mutable.PriorityQueue[((Int, Int), Int, Int)]()(Ordering.by(v => (-(v._2 + v._3))))
    frontier.enqueue((start._1, start._2, heuristic(start._1, end)))
    val visited = mutable.HashSet[((Int, Int), Set[(Int, Int)])]()
    var out: ((Int, Int), Int) = null
    while (frontier.nonEmpty) {
      val (pos, minute, heur) = frontier.dequeue()
      val currBlizz = blizzards(minute)
      if (pos == end) {
        // goal
        out = (pos, minute)
        frontier.clear()
      } else if (visited.add((pos, currBlizz))) {
        // search
        val nextBlizz = blizzards(minute + 1)
        val neighbors = directions.values
          .map { case (dx, dy) => (pos._1 + dx, pos._2 + dy) }
          .filterNot(walls.contains)
          .filterNot(nextBlizz.contains)
          .map(p => (p, minute + 1, heuristic(p, end)))
        frontier.addAll(neighbors)
        if (!nextBlizz.contains(pos)) {
          //wait
          frontier.addOne((pos, minute + 1, heur))
        }
      }
    }
    out
  }

}
