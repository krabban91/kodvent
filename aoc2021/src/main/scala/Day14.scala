import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day14 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (ins, start) = extractInput(strings)
    quantify(10, ins, start)
  }

  override def part2(strings: Seq[String]): Long = {
    val (instructions, input) = extractInput(strings)
    quantify(40, instructions, input)
  }

  private def extractInput(strings: Seq[String]): (Map[String, Seq[String]], Map[String, Long]) = {
    val start: String = strings.head
    val lastChar = start.last
    val ins: Map[String, Seq[String]] = strings.tail.tail.map(s => s.split(" -> ").toSeq).map(l => (l.head, Seq(l.head.substring(0, 1) + l.last, l.last + l.head.substring(1)))).toMap
    val input = mutable.HashMap[String, Long]()
    start.sliding(2).foreach(s => input.put(s, input.getOrElse(s, 0L) + 1L))
    input.put(lastChar.toString, 1)
    (ins, input.toMap)
  }

  private def quantify(steps: Int, instructions: Map[String, Seq[String]], input: Map[String, Long]): Long = {
    val counts = (0 until steps)
      .foldLeft(input)((curr, _) => step(curr, instructions))
      .groupBy(_._1.head)
      .map(t => (t._1, t._2.values.sum))
    counts.values.max - counts.values.min
  }

  private def step(curr: Map[String, Long], ins: Map[String, Seq[String]]): Map[String, Long] = {
    val next = mutable.HashMap[String, Long]()
    curr.foreachEntry((k, v) => {
      val o = ins.get(k)
      o.foreach(_.foreach(ok => next.put(ok, next.getOrElse(ok, 0L) + v)))
      if (o.isEmpty) {
        next.put(k, curr(k) + next.getOrElse(k, 0L))
      }
    })
    next.toMap
  }

}
