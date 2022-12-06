import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day06 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = packetStart(strings.head, 4)

  override def part2(strings: Seq[String]): Long = packetStart(strings.head, 14)

  def packetStart(string: String, minSize: Int): Int = string.sliding(minSize).zipWithIndex
    .filter(_._1.length >= minSize)
    .find(p => p._1 == p._1.distinct)
    .map(p => p._1.length + p._2)
    .get
}
