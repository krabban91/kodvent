package aoc.string

import aoc.input.Inputs

trait AoCPart2String extends Inputs {
  def part2(strings: Seq[String]): String

  def part2Result: String = part2(getInput)

  def printResultPart2: Unit = {
    println(s"${this.getClass.getName} - Live:\tPart 2 -> ${this.part2Result}")
  }
}
