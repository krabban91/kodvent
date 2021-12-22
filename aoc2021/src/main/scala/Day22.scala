import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day22 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(Instruction(_))
    val map = v.foldLeft(mutable.HashMap[(Int, Int, Int), Boolean]())((m, instr) => {
      (math.max(instr.xs._1, -50) to math.min(50, instr.xs._2))
        .foreach(x => (math.max(-50, instr.ys._1) to math.min(50, instr.ys._2))
          .foreach(y => (math.max(-50, instr.zs._1) to math.min(50, instr.zs._2))
            .foreach(z => m.put((x, y, z), instr.on))))
      m
    })
    map.count(x => x._2)
  }

  override def part2(strings: Seq[String]): Long = -1

  case class Instruction(on: Boolean, xs: (Int, Int), ys: (Int, Int), zs: (Int, Int)) {

  }

  object Instruction {
    def apply(string: String): Instruction = {
      val s = string.split(" ")
      val on = s.head == "on"
      val ss = s.last.split(",")
      val xs = ss.head.split("=").last.split("\\.\\.").map(_.toInt)
      val ys = ss.tail.head.split("=").last.split("\\.\\.").map(_.toInt)
      val zs = ss.last.split("=").last.split("\\.\\.").map(_.toInt)
      Instruction(on, (xs.head, xs.last), (ys.head, ys.last), (zs.head, zs.last))
    }
  }
}
