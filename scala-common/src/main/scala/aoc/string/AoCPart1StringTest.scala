package aoc.string

import aoc.input.TestInputs

trait AoCPart1StringTest extends AoCPart1String with TestInputs {
  def part1TestResult: String = part1(getInputTest)

  def printResultPart1Test: Unit = println(s"${this.getClass.getName} - Test:\tPart 1 -> ${this.part1TestResult}")
}
