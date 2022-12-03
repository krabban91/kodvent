import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day03 extends App with AoCPart1Test with AoCPart2Test {

  val A = "A".head.asInstanceOf[Int]
  val a = "a".head.asInstanceOf[Int]
  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(s => (s.take(s.length / 2), s.drop(s.length / 2)))
    val v2 = v.map { case (l, r) => l.find(c => r.contains(c)).get }
    val prios = v2.map(c => if (c.isUpper) (c.asInstanceOf[Int] - A) + 27 else c.asInstanceOf[Int] - a + 1)
    prios.sum
  }

  override def part2(strings: Seq[String]): Long = {
    val g = strings.grouped(3).toSeq
    val ps = g.map(l => l.last.find(v => l.head.filter(c => l.tail.head.contains(c)).contains(v)).get)

    val prios = ps.map(c => if (c.isUpper) (c.asInstanceOf[Int] - A) + 27 else c.asInstanceOf[Int] - a + 1)
    prios.sum
  }
}
