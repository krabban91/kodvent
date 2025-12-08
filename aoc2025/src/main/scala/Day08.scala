import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day08 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val isTest = strings.length == 20
    val count = if(isTest) 10 else 1000
    val allPoints = strings.map{s => val l = s.split(",")
      val p@(x,y,z) = (l.head.toLong, l.tail.head.toLong, l.last.toLong)
      p
    }.toSet
    def distance(a: (Long, Long, Long), b: (Long, Long, Long)) = {
      Math.sqrt(Math.pow(a._1 - b._1, 2) + Math.pow(a._2 - b._2, 2) + Math.pow(a._3 - b._3,2))
    }
    var it = 0
    val allDistances = allPoints.flatMap(a => allPoints.filter(_ != a).map(b => (Set(b, a), distance(a,b)))).toMap

    val circuits : Set[Set[(Long, Long, Long)]] = allPoints.map(Set(_))
    val after = (0 until count).foldLeft(circuits, Set[Set[(Long, Long, Long)]]()){ case ((grids, connected), i) =>
      val filtered = allDistances.filterNot(t => connected.contains(t._1))
      val shortest@(nodes, distance) = filtered.minBy(_._2)
      val selected = nodes.flatMap(s => grids.find(_.contains(s))).toSeq
      val (from, to) = (selected.head, selected.last)

      /*
      val best@(from, (to, (nodes, distance))): (Set[(Long, Long, Long)], (Set[(Long, Long, Long)], (Set[(Long, Long, Long)], Double))) = grids.map { g =>
        val other = grids.filter(_ != g)
        val v: (Set[(Long, Long, Long)], (Set[(Long, Long, Long)], Double)) = g.map(a => other.map(o => (o, o.map(b =>Set(a,b)).map(p => (p, allDistances(p))).minBy(_._2))).minBy(_._2._2)).minBy(_._2._2)
        (g, v)
      }.minBy(_._2._2._2)
       */
      val next = grids.filter(v  => v != from && v != to) ++ Set((from ++ to))
      println(s"Iteration ${it}:: Distance=${distance} Selected nodes=$nodes. product=${next.toSeq.map(_.size).sorted.reverse.take(3).product},  gridSizes=${next.map(_.size)}")
      it += 1
      (next, connected ++ Set(nodes))

    }
    // 2050324839043956736 is too high

    val value = after._1.toSeq.map(_.size.toLong).sorted.reverse
    val threeLargest = value.take(3)
    threeLargest.product

  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
