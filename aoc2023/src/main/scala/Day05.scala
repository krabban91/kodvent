import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day05 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Mapping(from: String, to: String, maps: Seq[MapList])
  case class MapList(destStart: Long, srcStart: Long, rangeLength: Long)

  object Mapping {
    def apply(str: String): Mapping = {
      val lines = str.split("\n")
      val hSplit = lines.head.split(" ").head.split("-to-")
      val (f, t) = (hSplit.head, hSplit.last)
      val ls = lines.tail.map(s => s.split(" ").map(_.toLong))
        .map(l => MapList(l(0), l(1), l(2))).toSeq
      Mapping(f,t, ls)
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val groups = groupsSeparatedByTwoNewlines(strings).map(_.stripPrefix("\n").stripSuffix("\n")).filter(_.nonEmpty)
    val seeds = groups.head.replace("seeds: ", "").split(" ").map(_.toLong)
    val maps = groups.tail.map(Mapping(_)).map(m => (m.from, m)).toMap
    val locs = lookup(Seq(seeds), maps)

    locs
  }

  private def lookup(seeds: Seq[Seq[Long]], maps: Map[String, Mapping]) = {
    var out = Long.MaxValue
    var i = 0
    var j = 0
    val t = mutable.HashMap[String, Long]()
    seeds.foreach(_.foreach { s =>
      s
      var from = "seed"
      var v = s
      t.put(from, s)
      while (from != "location") {
        val m = maps(from)
        val matched = m.maps.find(l => l.srcStart <= v && v < l.srcStart + l.rangeLength)
        v = matched.map(m => v - m.srcStart + m.destStart).getOrElse(v)
        t.put(m.to, v)
        from = m.to
      }
      if (v < out) {
        out = v
        j = j+1
        println(t)
      }
      i = i+1
      t.clear()
    })
    println(s"${seeds.size}, calculated $i values. decr $j times.")
    out
  }

  override def part2(strings: Seq[String]): Long = {
    val groups = groupsSeparatedByTwoNewlines(strings).map(_.stripPrefix("\n").stripSuffix("\n")).filter(_.nonEmpty)
    val seedRanges = groups.head.replace("seeds: ", "").split(" ").map(_.toLong)
    val seeds = (0 until seedRanges.length/2).map(_*2).map(i => (seedRanges(i) until seedRanges(i) + seedRanges(i+1)))
    val maps = groups.tail.map(Mapping(_)).map(m => (m.from, m)).toMap
    val locs = lookup(seeds, maps)
    locs
  }
}
