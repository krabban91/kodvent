

trait AoCPart2Test extends AoCPart2 with TestInputs {
  def part2TestResult: Long = part2(getInputTest)

  def printResultPart2Test: Unit = println(s"${this.getClass.getName} - Test:\tPart 1 -> ${this.part2TestResult}")
}
