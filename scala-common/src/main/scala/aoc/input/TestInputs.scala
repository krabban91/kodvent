package aoc.input

trait TestInputs extends Inputs {

  def getInputTest: Seq[String] = read(s"test/${this.getClass.getName.toLowerCase}.txt")
}
