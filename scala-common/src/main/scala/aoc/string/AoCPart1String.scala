package aoc.string

import aoc.input.Inputs

trait AoCPart1String extends Inputs {
  def part1(strings: Seq[String]): String

  def part1Result: String = part1(getInput)

  def printResultPart1: Unit = println(s"${this.getClass.getName} - Live  :\tPart 1 -> ${this.part1Result}")
}
