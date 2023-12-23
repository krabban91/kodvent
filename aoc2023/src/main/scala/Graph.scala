import scala.collection.mutable

object Graph {

  def shortestPath[Point](starts: Seq[Point], end: Point => Boolean, heuristic: Point => Long, neighbors: Point => Seq[(Point, Long)]): (Long, Seq[Point]) = {
    val queue = mutable.PriorityQueue[(Point, Long, Long)]()(Ordering.by(v => -(v._2 + v._3)))
    queue.addAll(starts.map(p => (p, 0L, heuristic(p))))
    val visited = mutable.HashSet[Point]()
    val cameFrom: mutable.Map[(Point, Long), (Point, Long)] = mutable.HashMap[(Point, Long), (Point, Long)]()
    while (queue.nonEmpty) {
      val (pos, cost, _) = queue.dequeue()
      if (end.apply(pos)) {
        val map = cameFrom.toMap
        val path = derivePath(pos, cost, map)

        return (cost, path)
      }
      if (visited.add(pos)) {
        neighbors(pos)
          .filterNot(t => visited.contains(t._1))
          .foreach { p =>
            queue.enqueue((p._1, cost + p._2, heuristic(p._1)))
            cameFrom.put((p._1, cost + p._2), (pos, cost))
          }
      }
    }
    (Long.MaxValue, Seq())
  }

  def flood[Point](starts: Seq[Point], neighbors: Point => Seq[(Point, Long)]): Map[Point, Long] = {
    val queue = mutable.PriorityQueue[(Point, Long)]()(Ordering.by(-_._2))
    queue.addAll(starts.map((_, 0L)))
    val visited = mutable.HashMap[Point, Long]()
    while (queue.nonEmpty) {
      val (pos, cost) = queue.dequeue()
      if (!visited.contains(pos)) {
        visited.put(pos, cost)
        neighbors(pos)
          .filterNot(t => visited.contains(t._1))
          .foreach { p => queue.enqueue((p._1, cost + p._2)) }
      }
    }
    visited.toMap
  }

  def stepAround[Point](starts: Seq[Point], neighbors: Point => Seq[(Point, Long)], steps: Long): Seq[Point] = {

    val queue = mutable.PriorityQueue[(Point, Long)]()(Ordering.by(-_._2))
    queue.addAll(starts.map((_, 0L)))
    val visited = mutable.HashSet[(Point, Long)]()
    while (queue.nonEmpty) {
      val pop@(pos, cost) = queue.dequeue()
      if (cost > steps) {
        // continue
      } else {
        if (visited.add((pop))) {
          neighbors(pos)
            .map(p => (p._1, p._2 + cost))
            .filterNot(visited.contains)
            .foreach(p => queue.enqueue(p))
        }
      }
    }
    visited.toSeq.filter(_._2 == steps).map(_._1).distinct
  }

  private def derivePath[Point](to: Point, cost: Long, paths: Map[(Point, Long), (Point, Long)]): Seq[Point] = {
    val out = mutable.ListBuffer[Point]()
    var loc = (to, cost)
    out.addOne(loc._1)
    while (paths.contains(loc)) {
      loc = paths(loc)
      out.addOne(loc._1)
    }
    out.toSeq.reverse
  }
}
