import scala.collection.mutable

object Graph {

  def shortestPath[Point](starts: Seq[Point], end: Point => Boolean, heuristic: Point => Long, neighbors: Point => Seq[(Point, Long)]): Long = {
    val queue = mutable.PriorityQueue[(Point, Long, Long)]()(Ordering.by(v => -(v._2 + v._3)))
    queue.addAll(starts.map(p => (p, 0L, heuristic(p))))
    val visited = mutable.HashSet[Point]()
    while (queue.nonEmpty) {
      val pop = queue.dequeue()
      if (end.apply(pop._1)) {
        return pop._2
      }
      if (visited.add(pop._1)) {
        neighbors.apply(pop._1)
          .filterNot(t => visited.contains(t._1))
          .foreach(p => queue.enqueue((p._1, pop._2 + p._2, heuristic(p._1))))
      }
    }
    Long.MaxValue
  }
}
