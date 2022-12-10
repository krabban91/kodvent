import aoc.numeric.AoCPart1Test
import aoc.string.AoCPart2StringTest

import scala.collection.mutable.ListBuffer

object Day10 extends App with AoCPart1Test with AoCPart2StringTest {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val interesting = Seq(20, 60, 100, 140, 180, 220)
    val values: Seq[Long] = registers(strings)
    interesting.map(i => values(i - 1) * i).sum
  }

  override def part2(strings: Seq[String]): String = {
    val c = Map(true -> "#", false -> " ")
    val values = registers(strings)
    "\n" + values.zipWithIndex.map(t => (t._1, t._2 % 40 + 1))
      .map((matched _).tupled(_))
      .map(c)
      .take(240).grouped(40).map(_.reduce(_ + _)).mkString("\n")
  }

  private def registers(strings: Seq[String]) = {
    val values = ListBuffer[Long]()
    var register = 1
    val addPattern = """addx (-?\d+)""".r
    var pointer = 0
    (0 to 240).foreach(i => {

      val op = strings((pointer % (strings.length)))

      op match {
        case addPattern(v) =>
          values.append(register)
          values.append(register)
          register += v.toInt
        case _ =>
          values.append(register)
      }
      pointer += 1
    })
    values.toSeq
  }

  private def matched(register: Long, p: Int): Boolean = {
    p >= register && p <= register + 2
  }
}
