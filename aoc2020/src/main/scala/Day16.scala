import aoc.numeric.{AoCPart1Test, AoCPart2}

import scala.collection.mutable

object Day16 extends App with AoCPart1Test with AoCPart2 {

  override def part1(strings: Seq[String]): Long = {
    val (rules, your, theirs) = input(strings)
    theirs.flatten
      .filterNot(v => rules
        .exists(r => r.intervals
          .exists(i => v >= i._1 && v <= i._2)))
      .sum
  }

  override def part2(strings: Seq[String]): Long = {
    val (rules, your, theirs) = input(strings)
    val filtered = theirs.filter(l => l.forall(v => rules.exists(r => r.fits(v))))
    viableMatching(matchingColumns(rules, filtered))
      .map(_
        .filter(t => t._1.name.startsWith("departure"))
        .values
        .map(i => your(i))
        .product)
      .getOrElse(-1)

  }

  def input(strings: Seq[String]): Tuple3[Seq[Rule], Seq[Long], Seq[Seq[Int]]] = {
    val f = strings.indexOf("")
    val rules = strings.take(f).map(Rule(_))
    val ff = strings.indexOf("", f + 1)
    val your = strings.slice(f + 2, ff).head.split(",").map(_.toLong)
    val theirs = strings.drop(ff + 2).map(_.split(",").map(_.toInt).toSeq)
    (rules, your, theirs)
  }

  def matchingColumns(rules: Seq[Rule], tickets: Seq[Seq[Int]]): Map[Rule, Seq[Int]] = rules
    .map(r => r -> rules
      .indices
      .filter(i => tickets
        .map(l => l(i))
        .forall(r.fits)))
    .toMap

  def viableMatching(map: Map[Rule, Seq[Int]]): Option[Map[Rule, Int]] = {
    val matched = mutable.ListBuffer[Tuple2[Rule, Seq[Int]]]()
    val queue = mutable.PriorityQueue.from(map.keys)(Ordering.by[Rule, Int](r => r.possibleColumns(map, matched).size).reverse)
    while (queue.nonEmpty) {
      val r = queue.dequeue()
      matched += (r -> r.possibleColumns(map, matched))
    }
    if (matched.forall(_._2.size == 1)) {
      Option(matched.map(t => (t._1, t._2.head)).toMap)
    } else None
  }

  case class Rule(name: String, intervals: Seq[(Int, Int)]) {
    def fits(v: Int): Boolean = intervals.exists(i => v >= i._1 && v <= i._2)

    def possibleColumns(map: Map[Rule, Seq[Int]], matched: Iterable[(Rule, Seq[Int])]): Seq[Int] = map(this).filterNot(v => matched.exists(_._2.contains(v)))
  }

  object Rule {
    def apply(in: String): Rule = {
      val name = in.split(":")(0)
      val v = in.split(":")(1).trim.split("or").map(_.trim).map(s => s.split("-")(0).toInt -> s.split("-")(1).toInt)
      Rule(name, v)

    }
  }

}
