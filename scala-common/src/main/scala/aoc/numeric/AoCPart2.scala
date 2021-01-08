package aoc.numeric

import aoc.input.Inputs

trait AoCPart2 extends Inputs {
  def part2(strings: Seq[String]): Long

  def part2Result: Long = part2(getInput)

  def printResultPart2: Unit = println(s"${this.getClass.getName} - Live:\tPart 2 -> ${this.part2Result}")
}
