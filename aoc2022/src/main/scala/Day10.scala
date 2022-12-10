import aoc.numeric.AoCPart1Test
import aoc.string.AoCPart2StringTest
import computer.{CommunicationsUnit, Instruction}

object Day10 extends App with AoCPart1Test with AoCPart2StringTest {

  override def part1(strings: Seq[String]): Long = units(strings.map(Instruction(_)))
    .take(220).grouped(40).toSeq
    .map(_ (19)).map(unit => unit.cycle * unit.registerX)
    .sum

  override def part2(strings: Seq[String]): String = {
    val c = Map(true -> "#", false -> " ")
    "\n" + units(strings.map(Instruction(_))).map(_.registerX)
      .zipWithIndex.map(t => (t._1, t._2 % 40 + 1))
      .map((matched _).tupled(_))
      .map(c)
      .take(240).grouped(40).map(_.reduce(_ + _)).mkString("\n")
  }

  private def units(operations: Seq[Instruction]): Seq[CommunicationsUnit] = {
    val start = CommunicationsUnit(1, None, 0, operations, 1)
    CommunicationsUnit.stepCycles(start, 240)
  }

  private def matched(register: Int, p: Int): Boolean = {
    p >= register && p <= register + 2
  }


}
