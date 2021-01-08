package aoc.numeric

import aoc.input.TestInputs


trait AoCPart1Test extends AoCPart1 with TestInputs {
  def part1TestResult: Long = part1(getInputTest)

  def printResultPart1Test: Unit = println(s"${this.getClass.getName} - Test:\tPart 1 -> ${this.part1TestResult}")
}
