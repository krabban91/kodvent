import aoc.numeric.AoCPart1Test

import scala.collection.mutable

object Day25 extends App with AoCPart1Test {

  printResultPart1Test
  printResultPart1

  override def part1(strings: Seq[String]): Long = {
    monteCarloSolve(strings)
  }

  def monteCarloSolve(strings: Seq[String], count: Long = 10) : Long = {
    val cs = strings.map(Component(_))
    val connections = cs.flatMap(c => c.connectsTo.map(o => Set((c.id, o))))
    val graph = connections.flatMap { s => s.flatMap(v => Seq((v._1, v._2), (v._2, v._1))) }.groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2)))

    val mostUsed = mutable.HashMap[Set[String], Long]()
    graph.foreach(kv => kv._2.foreach(v => mostUsed.put(Set(kv._1, v), 0L)))

    val nodes = graph.keys.toSeq
    // Some rng in my life!
    (0L to count).foreach { i =>
      val (from, to) = sample(nodes)
      val taken = path(from, to, graph)
      val edges = taken.sliding(2)
      edges.foreach(e => {
        val set = e.toSet
        mostUsed.put(set, mostUsed(set) + 1L)
      })
    }

    val top3 = mostUsed.toSeq.sortBy(_._2).takeRight(3).reverse
    val modified = top3.foldLeft(graph) { case (outG, (set, dist)) =>
      val value = outG.map { case (k, vs) => (k, vs.filterNot(v => Set(k, v) == set)) }
      value
    }
    val e = top3(1)._1.toSeq
    val (eL, eR) = (e.head, e.last)
    val (l, r) = (Graph.flood[String](Seq(eL), modified(_).map((_, 1))), Graph.flood[String](Seq(eR), modified(_).map((_, 1))))
    val (sizeL, sizeR) = (l.size, r.size)
    if (sizeL + sizeR != graph.size) {
      println("trying again")
      monteCarloSolve(strings, count * 2)
    } else {
      sizeL.toLong * sizeR.toLong
    }
  }

  def sample(vertices: Seq[String]): (String, String) = {
    val from = vertices((math.random() * vertices.size).toInt)
    val to = vertices((math.random() * vertices.size).toInt)
    if (from == to) {
      sample(vertices)
    } else (from, to)
  }

  def path(from: String, to: String, graph: Map[String, Seq[String]]): Seq[String] = {
    // cost is random for random walk
    Graph.shortestPath[String](Seq(from), _ == to, _ => 0, graph(_).map((_, (math.random() * 10).toLong)))._2
  }

  case class Component(id: String, connectsTo: Seq[String])

  object Component {
    def apply(str: String): Component = {
      val s = str.split(":")
      val p = s.last.split(" ").map(_.strip()).filterNot(_.isEmpty)
      Component(s.head.strip(), p)
    }
  }
}
