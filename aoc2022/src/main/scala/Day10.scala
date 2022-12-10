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
    val values = ListBuffer[Long]()
    var register = 1
    val addPattern = """addx (\d+)""".r
    val minusPattern = """addx -(\d+)""".r
    var pointer = 0
    val cycles = 0 to interesting.last
    cycles.foreach(i => {

      val op = strings((pointer % (strings.length)))

      op match {
        case addPattern(v) =>
          values.append(register)
          values.append(register)
          register += v.toInt
        case minusPattern(v) =>
          values.append(register)
          values.append(register)
          register -= v.toInt
        case _ =>
          values.append(register)
      }
      pointer += 1
    })
    val value = interesting.map(i => values(i - 1) * i)
    value.sum
  }

  override def part2(strings: Seq[String]): String = {
    val interesting = Seq(20, 60, 100, 140, 180, 220)
    val values = ListBuffer[Long]()
    var register = 1
    val addPattern = """addx (\d+)""".r
    val minusPattern = """addx -(\d+)""".r
    var pointer = 0
    val cycles = 0 to interesting.last
    var cycle = 0
    cycles.foreach(i => {

      val op = strings((pointer % (strings.length)))

      val endLine = (c: Int) => (c) % 40 == 0
      val point = (c: Int) => (c) % 40
      op match {
        case addPattern(v) =>
          values.append(register)
          cycle += 1
          var p = point(cycle)
          if (matched(register, p)) {
            print("#")
          } else {
            print(".")
          }
          if (endLine(cycle)) {
            println()
          }
          values.append(register)
          cycle += 1
          p = point(cycle)
          if (matched(register, p)) {
            print("#")
          } else {
            print(".")
          }
          if (endLine(cycle)) {
            println()
          }
          register += v.toInt
        case minusPattern(v) =>
          values.append(register)
          cycle += 1
          var p = point(cycle)
          if (matched(register, p)) {
            print("#")
          } else {
            print(".")
          }
          if (endLine(cycle)) {
            println()
          }
          values.append(register)
          cycle += 1
          p = point(cycle)
          if (matched(register, p)) {
            print("#")
          } else {
            print(".")
          }
          if (endLine(cycle)) {
            println()
          }
          register -= v.toInt
        case _ =>
          values.append(register)
          cycle += 1
          val p = point(cycle)
          if (matched(register, p)) {
            print("#")
          } else {
            print(".")
          }
          if (endLine(cycle)) {
            println()
          }
      }
      pointer += 1
    })
    "\n" + values
      .zipWithIndex.map { case (v, i) => matched(v.toInt, i % 40 + 1) }
      .map(if (_) "#" else " ")
      .take(240).grouped(40).map(_.reduce(_ + _)).mkString("\n")
  }

  private def matched(register: Int, p: Int): Boolean = {
    p >= register && p <= register + 2
  }
}
