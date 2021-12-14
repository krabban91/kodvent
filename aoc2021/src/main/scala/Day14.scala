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
    val start: String = strings.head
    val ins: Map[String, String] = strings.tail.tail.map(s=> s.split(" -> ").toSeq).map(l => (l.head, l.last)).toMap
    var curr = start

    for (i <- 1 to 10){
      val v  = curr.chars().mapToObj(_.toChar.toString).collect(Collectors.toList[String]).asScala.toSeq
      val next = v.sliding(2).map(xs=> xs.reduce(_+_)).map(s=> s.substring(0,1) + ins.getOrElse(s, "")).reduce(_+_) + v.last
      curr = next
    }
    val o: Seq[Char] = curr.chars().mapToObj(_.toChar).collect(Collectors.toList[Char]).asScala.toSeq
    val res = mutable.HashMap[Char, Long]()
    o.foreach(c => res.put(c, res.getOrElse(c, 0L) + 1L))
    res.values.max - res.values.min
  }

  override def part2(strings: Seq[String]): Long = -1
}
