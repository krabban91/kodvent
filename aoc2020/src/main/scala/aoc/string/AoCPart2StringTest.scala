package aoc.string

import aoc.input.TestInputs

trait AoCPart2StringTest extends AoCPart2String with TestInputs {
  def part2TestResult: String = part2(getInputTest)
  def printResultPart2Test: Unit = println(s"${this.getClass.getName} - Test:\tPart 1 -> ${this.part2TestResult}")
}
