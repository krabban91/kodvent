import aoc.numeric.AoCPart1Test
import aoc.string.AoCPart2StringTest
import computer.{CommunicationsUnit, Instruction}

object Day10 extends App with AoCPart1Test with AoCPart2StringTest {

  override def part1(strings: Seq[String]): Long = {
    val interesting = Seq(20, 60, 100, 140, 180, 220)
    val values: Seq[Long] = registers(strings.map(Instruction(_)))
    interesting.map(i => values(i - 1) * i).sum
  }

  override def part2(strings: Seq[String]): String = {
    val c = Map(true -> "#", false -> " ")
    "\n" + registers(strings.map(Instruction(_)))
      .zipWithIndex.map(t => (t._1, t._2 % 40 + 1))
      .map((matched _).tupled(_))
      .map(c)
      .take(240).grouped(40).map(_.reduce(_ + _)).mkString("\n")
  }

  private def registers(operations: Seq[Instruction]): Seq[Long] = {
    val start = CommunicationsUnit(1, None, 0, operations)
    CommunicationsUnit.stepCycles(start, 240).map(_.registerX)
  }

  private def matched(register: Long, p: Int): Boolean = {
    p >= register && p <= register + 2
  }


}
