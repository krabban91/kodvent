import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import java.util.stream.Collectors
import scala.collection.mutable
import scala.jdk.CollectionConverters.CollectionHasAsScala

object Day14 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (ins, start) = extractInput(strings)
    quantify(10, ins, start)
  }

  override def part2(strings: Seq[String]): Long = -1


  private def extractInput(strings: Seq[String]): (Map[String, String], String) = {
    val start: String = strings.head
    val ins: Map[String, String] = strings.tail.tail.map(s=> s.split(" -> ").toSeq).map(l => (l.head, l.last)).toMap
    (ins, start)
  }

  private def quantify(steps: Int, instructions: Map[String, String], input: String): Long = {
    var curr = input
    for (i <- 1 to steps){
      val v  = curr.chars().mapToObj(_.toChar.toString).collect(Collectors.toList[String]).asScala.toSeq
      val next = v.sliding(2).map(xs=> xs.reduce(_+_)).map(s=> s.substring(0,1) + instructions.getOrElse(s, "")).reduce(_+_) + v.last
      curr = next
    }
    val o: Seq[Char] = curr.chars().mapToObj(_.toChar).collect(Collectors.toList[Char]).asScala.toSeq
    val res = mutable.HashMap[Char, Long]()
    o.foreach(c => res.put(c, res.getOrElse(c, 0L) + 1L))
    res.values.max - res.values.min
  }

  private def step(curr: String, ins: Map[String, Seq[String]]): String = {
    val v  = curr.chars().mapToObj(_.toChar.toString).collect(Collectors.toList[String]).asScala.toSeq
    v.sliding(2).map(xs=> xs.reduce(_+_)).map(s=> s.substring(0,1) + ins.getOrElse(s, "")).reduce(_+_) + v.last
  }
}
