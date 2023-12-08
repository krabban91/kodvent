import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day08 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val g = groupsSeparatedByTwoNewlines(strings)
    val instr = g.head.strip()
    val m = g.last.split("\n").map(_.strip()).filter(_.nonEmpty)
      .map(s => (s.split(" = ").head.strip(),(s.split(" = ").last.split(", ").head.stripPrefix("("), s.split(" = ").last.split(", ").last.stripSuffix(")"))))
      .toMap
    val start = "AAA"
    val end = "ZZZ"
    var steps = 0
    var curr = start
    while (curr != end) {
      curr = instr(steps % instr.length) match {
        case 'L' => m(curr)._1
        case 'R' => m(curr)._2
      }
      steps = steps + 1
    }
    steps.toLong
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
