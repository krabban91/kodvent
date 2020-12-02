package aoc.numeric

import aoc.input.Inputs

trait AoCPart1 extends Inputs {
  def part1(strings: Seq[String]): Long

  def part1Result: Long = part1(getInput)

  def printResultPart1: Unit = println(s"${this.getClass.getName} - Live:\tPart 1 -> ${this.part1Result}")
}
