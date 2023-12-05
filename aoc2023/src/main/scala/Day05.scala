import aoc.numeric.{AoCPart1Test, AoCPart2Test}

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
        .map(l => MapList(l.head, l.tail.head, l.last)).toSeq
      Mapping(f,t, ls)
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val groups = groupsSeparatedByTwoNewlines(strings).map(_.stripPrefix("\n").stripSuffix("\n")).filter(_.nonEmpty)
    val seeds = groups.head.replace("seeds: ", "").split(" ").map(_.toLong)
    val maps = groups.tail.map(Mapping(_)).map(m => (m.from, m)).toMap
    val locs = seeds.map{s => s
      var from = "seed"
      var v = s
      while(from != "location") {
        val m = maps(from)
        val matched = m.maps.find(l => l.srcStart <= v && v <= l.srcStart + l.rangeLength).getOrElse(MapList(0,0,0))
        from = m.to
        val delta = matched.destStart - matched.srcStart
        v = v + delta
      }
      v
    }
    locs.min
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
