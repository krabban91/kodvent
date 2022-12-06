import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day06 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = packetStart(strings.head, 4)

  override def part2(strings: Seq[String]): Long = packetStart(strings.head, 14)

  def packetStart(string: String, minSize: Int): Int = string.foldLeft(("", 0, false)) { case ((str, count, seen), c) => if (seen)
    (str, count, seen)
  else if (str.contains(c))
    (str.drop(str.indexOf(c) + 1) + c, count + 1, seen)
  else (str + c, count + 1, str.length + 1 >= minSize)
  }._2
}
