import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import java.util.stream
import scala.collection.mutable
object Day08 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val easy = Set(2, 4, 3, 7)
    strings
      .map(s => s.split("\\|").last.split(" ").map(_.strip()))
      .map(l => l.count(s => easy.contains(s.length)))
      .sum
  }

  override def part2(strings: Seq[String]): Long = {
    strings.map(s => {
      val display = s.split("\\|").last.split(" ").map(_.strip()).filter(_.nonEmpty).map(normalize)
      val mapping = extractMapping(s.split("\\|").flatMap(p => p.split(" ").map(_.strip()).filter(_.nonEmpty).map(normalize)).toSet)
      display.map(mapping)
        .reverse.zipWithIndex.map(t => math.pow(10, t._2).toLong * t._1)
        .sum
    }).sum
  }

  def normalize(string: String): String = {
    val v: stream.Stream[String] = string.chars().sorted().mapToObj(_.toChar.toString)
    val res = v.reduce(_+_).orElse("")
    res
  }

  def extractMapping(strings: Set[String]): Map[String, Int] = {
    val output = mutable.HashMap[String, Int]()
    val lookup = mutable.HashMap[Int, String]()
    strings.find(_.length == 2).foreach(s => {
      output.put(s, 1)
      lookup.put(1, s)
    })
    strings.find(_.length == 4).foreach(s => {
      output.put(s, 4)
      lookup.put(4, s)
    })
    strings.find(_.length == 3).foreach(s => {
      output.put(s, 7)
      lookup.put(7, s)
    })
    strings.find(_.length == 7).foreach(s => {
      output.put(s, 8)
      lookup.put(8, s)
    })

    val gs = strings.groupBy(_.length)

    var sixes = gs(6)
    sixes
      .find(s => fullOverlap(lookup(7), s) && !fullOverlap(lookup(4), s))
      .foreach(s => {
      output.put(s, 0)
      lookup.put(0, s)
      sixes = sixes.filterNot(_ == s)
    })
    sixes
      .find(s => fullOverlap(lookup(7), s) && fullOverlap(lookup(4), s))
      .foreach(s => {
      output.put(s, 9)
      lookup.put(9, s)
      sixes = sixes.filterNot(_ == s)
    })
    sixes.find(_=> true).foreach(s => {
      output.put(s, 6)
      lookup.put(6, s)
      sixes = sixes.filterNot(_ == s)
    })

    var fives = gs(5)
    fives.find(s => fullOverlap(lookup(7), s)).foreach(s => {
      output.put(s, 3)
      lookup.put(3, s)
      fives = fives.filterNot(_ == s)
    })
    fives.find(s => fullOverlap(s, lookup(9))).foreach(s => {
      output.put(s, 5)
      lookup.put(5, s)
      fives = fives.filterNot(_ == s)
    })
    fives.find(_=> true).foreach(s => {
      output.put(s, 2)
      lookup.put(2, s)
      fives = fives.filterNot(_ == s)
    })
    output.toMap
  }

  def fullOverlap(inner: String, outer: String): Boolean = {
    inner.forall(outer.contains(_))
  }

}
