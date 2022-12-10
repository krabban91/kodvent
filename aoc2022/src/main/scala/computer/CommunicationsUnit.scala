package computer

case class CommunicationsUnit(registerX: Int, currentOperation: Option[(Instruction, Int)], pointer: Int, operations: Seq[Instruction], cycle: Int) {

  def step(): CommunicationsUnit = {
    val (op, time) = currentOperation.getOrElse((operations(pointer), 0))

    if (op.cycles == time + 1) {
      val nextPointer = (pointer + 1) % operations.size
      op match {
        case Add(value) => CommunicationsUnit(registerX + value, None, nextPointer, operations, cycle + 1)
        case NoOp() => CommunicationsUnit(registerX, None, nextPointer, operations, cycle + 1)
      }
    } else {
      CommunicationsUnit(registerX, Some((op, time + 1)), pointer, operations, cycle + 1)
    }
  }
}

object CommunicationsUnit {
  def stepCycles(start: CommunicationsUnit, n: Int): Seq[CommunicationsUnit] = (0 until n).foldLeft((start, Seq[CommunicationsUnit]())) {
    case ((cpu, values), _) =>
      (cpu.step(), values ++ Seq(cpu))
  }._2
}
