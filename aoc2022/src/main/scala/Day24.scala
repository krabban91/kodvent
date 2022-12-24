import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day24 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  def nextBlizzardsMap(blizzard: Seq[((Int, Int), Char)], v: Map[(Int, Int), Char], directions: Map[Char, (Int, Int)]): Seq[((Int, Int), Char)] = {
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

  def heuristic(start: (Int, Int), end: (Int, Int)): Int = math.abs(start._1 - end._1) + math.abs(start._2 - end._2)

  override def part1(strings: Seq[String]): Long = {
    val v = strings.zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.map { case (c, x) => ((x, y), c) } }.toMap
    val directions = Map(
      '>' -> (1, 0),
      'v' -> (0, 1),
      '<' -> (-1, 0),
      '^' -> (0, -1))
    val blizzards0: Seq[((Int, Int), Char)] = v.filter(kv => directions.contains(kv._2)).toSeq
    val minY = v.keySet.map(_._2).min
    val maxY = v.keySet.map(_._2).max

    val start = v.find(kv => kv._1._2 == minY && kv._2 == '.').map(_._1).get
    val end = v.find(kv => kv._1._2 == maxY && kv._2 == '.').map(_._1).get
    val blizzards = mutable.HashMap[Int, Seq[((Int, Int), Char)]]()
    blizzards.put(0, blizzards0)

    val frontier = mutable.PriorityQueue[((Int, Int), Int)]()(Ordering.by(v => (-(v._2))))
    frontier.enqueue((start, 0))
    val visited = mutable.HashSet[((Int, Int), Seq[((Int, Int), Char)])]()
    while (frontier.nonEmpty) {
      val pop = frontier.dequeue()
      val currBlizz = blizzards(pop._2)
      if (currBlizz.exists(kv => kv._1 == pop._1)){
        //froze
      } else if (pop._1 == end) {
        // goal
        return pop._2
      } else if (visited.add((pop._1, currBlizz))) {
        // search
        if (!blizzards.contains(pop._2 + 1)) {
          val nextMinuteMap = nextBlizzardsMap(blizzards(pop._2), v, directions)
          blizzards.put(pop._2 + 1, nextMinuteMap)
        }

        val nextBlizz = blizzards(pop._2 + 1)

        val neighbors = directions.values
          .map { case (dx, dy) => (pop._1._1 + dx, pop._1._2 + dy) }
          .filterNot(p => v.getOrElse(p, '#') == '#')
          .filterNot(p => nextBlizz.exists(kv => kv._1 == p))
          .map(p => (p, pop._2 + 1))
        frontier.addAll(neighbors)
        if (!currBlizz.exists(kv => kv._1 == pop._1)){
          //wait
          frontier.addOne((pop._1, pop._2 + 1))
        }


      }
    }
    -v.size
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
