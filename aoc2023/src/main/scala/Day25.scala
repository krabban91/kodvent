import aoc.numeric.AoCPart1Test

import scala.collection.mutable

object Day25 extends App with AoCPart1Test {

  printResultPart1Test
  printResultPart1


  override def part1(strings: Seq[String]): Long = {
    val graph = parse(strings)
    val t0 = System.nanoTime()
    val b@(l, r) = kargerSteinSolve(graph)
    val t1 = System.nanoTime()

    val a@(l0, r0) = monteCarloSolve(graph)
    val t2 = System.nanoTime()
    val ksTime = (t1 - t0).toDouble / 1e9
    val mcTime = (t2 - t1).toDouble / 1e9
    println("Solved with Karger-Stein after %.2f seconds".format(ksTime))
    println("Solved with Monte-Carlo after %.2f seconds".format(mcTime))
    l.size * r.size
  }

  def kargerSteinSolve(graph: Map[String, Component], count: Long = 3): (Set[String], Set[String]) = {
    val edges = graph.toSeq.flatMap(kv => kv._2.connectsTo.map(Set(kv._1, _))).distinct

    var (l, r) = kargerStein(graph, edges)
    while (countCuts(Set(l, r), edges) != count) {
      val next = kargerStein(graph, edges)
      l = next._1
      r = next._2
    }
    (l, r)
  }

  private def kargerStein(graph: Map[String, Component], edgeSet: Seq[Set[String]]): (Set[String], Set[String]) = {
    val edges = edgeSet
    val subsets = mutable.HashSet[mutable.HashSet[String]]()
    subsets.addAll(graph.keySet.map{s => val set = mutable.HashSet[String](); set.add(s); set})
    while (subsets.toSeq.count(_.nonEmpty) > 2) {
      val seq = edges((edges.length * math.random()).toInt).toSeq
      val (from, to) = (seq.head, seq.last)
      val l = subsets.find(s => s.contains(from)).get
      val r = subsets.find(s => s.contains(to)).get
      if (l == r) {
      } else {
        l.addAll(r)
        r.clear()
        subsets.remove(r)
      }
    }
    val temp = subsets.toSeq.filter(_.nonEmpty)
    (temp.head.toSet, temp.last.toSet)
  }

  private def countCuts(sets: Set[Set[String]], edges: Seq[Set[String]]): Long = {
    edges.foldLeft(0L){case (cuts, edge) =>
      val seq = edge.toSeq
      val (from, to) = (seq.head, seq.last)
      val first = sets.find(_.contains(from)).get
      val second = sets.find(_.contains(to)).get
      if (first == second) cuts else {
        (cuts + 1L)
      }
    }
  }

  def monteCarloSolve(graph: Map[String, Component], count: Long = 50): (Set[String], Set[String]) = {
    val mostUsed = mutable.HashMap[Set[String], Long]()
    graph.foreach(kv => kv._2.connectsTo.foreach(v => mostUsed.put(Set(kv._1, v), 0L)))

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
      val value = outG.map { case (k, vs) => (k, Component(k, vs.connectsTo.filterNot(v => Set(k, v) == set))) }
      value
    }
    val e = top3(1)._1.toSeq
    val (eL, eR) = (e.head, e.last)
    val (l, r) = (Graph.flood[String](Seq(eL), modified(_).connectsTo.map((_, 1))).keySet, Graph.flood[String](Seq(eR), modified(_).connectsTo.map((_, 1))).keySet)
    val (sizeL, sizeR) = (l.size, r.size)
    if (sizeL + sizeR != graph.size) {
      println("trying again")
      monteCarloSolve(graph, count * 2)
    } else {
      (l, r)
    }
  }

  private def parse(strings: Seq[String]) = {
    val cs = strings.map(Component(_))
    val connections = cs.flatMap(c => c.connectsTo.map(o => Set((c.id, o))))
    val graph = connections.flatMap { s => s.flatMap(v => Seq((v._1, v._2), (v._2, v._1))) }.groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2)))
    val allEdges = graph.map(kv => (kv._1, Component(kv._1, kv._2)))
    allEdges
  }

  def sample(vertices: Seq[String]): (String, String) = {
    val from = vertices((math.random() * vertices.size).toInt)
    val to = vertices((math.random() * vertices.size).toInt)
    if (from == to) {
      sample(vertices)
    } else (from, to)
  }

  def path(from: String, to: String, graph: Map[String, Component]): Seq[String] = {
    // cost is random for random walk
    Graph.shortestPath[String](Seq(from), _ == to, _ => 0, graph(_).connectsTo.map((_, (math.random() * 10).toLong)))._2
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
