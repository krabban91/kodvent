import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable.ListBuffer

object Day10 extends App with AoCPart1Test with AoCPart2Test {

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
    var time = 0
    var add = 0
    var pointer = 0
    val cycles = 0 to interesting.last
    cycles.foreach(i => {

      val op = strings((pointer % (strings.length)))

        op match {
          case addPattern(v) =>
            values.append(register)
            values.append(register)
            register +=v.toInt
          case minusPattern(v) =>
            values.append(register)
            values.append(register)
            register -=v.toInt
          case _ =>
            values.append(register)
        }
        pointer += 1
    })
    val value = interesting.map(i => values(i - 1) * i)
    value.sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
