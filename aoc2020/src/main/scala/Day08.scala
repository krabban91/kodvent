import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day08 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val program = strings.map(Instruction(_))
    runConsule(program, strict = false).get
  }

  override def part2(strings: Seq[String]): Long = {
    val input = strings.map(Instruction(_))
    for (i <- input.indices.filter(i => Seq("nop", "jmp").contains(input(i).operation))) {
      val program: Seq[Instruction] = withSwappedInstruction(input, i)
      val result = runConsule(program, strict = true)
      if(result.isDefined) {
        return result.get
      }
    }
    -1
  }

  private def runConsule(program: Seq[Instruction], strict: Boolean): Option[Int] = {
    var accumulator = 0
    var pointer = 0
    val setOps = mutable.Set[Int]()
    while (!setOps.contains(pointer) && pointer < program.size) {
      setOps.add(pointer)
      val op = program(pointer)
      if (op.operation.equals("acc")) {
        accumulator += op.value
        pointer += 1
      } else if (op.operation.equals("jmp")) {
        pointer += op.value
      } else if (op.operation.equals("nop")) {
        pointer += 1
      }
    }
    (strict, setOps.contains(pointer)) match {
      case (true, true) => None
      case _ => Option(accumulator)
    }
  }

  private def withSwappedInstruction(input: Seq[Instruction], index: Int): Seq[Instruction] = {
    val program = mutable.ListBuffer.from(input)
    program.update(index,
      if (input(index).operation.equals("nop")) Instruction("jmp", input(index).value)
      else Instruction("nop", input(index).value))
    program.toSeq
  }

  case class Instruction(operation: String, value: Int)

  object Instruction {
    def apply(string: String): Instruction = {
      val pair = string.split("\\s+")
      Instruction(pair(0), pair(1).toInt)
    }
  }

}
