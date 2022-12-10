package computer

trait Instruction {
  def cycles: Int
}

object Instruction {

  def apply(string: String): Instruction = {
    val addPattern = """addx (-?\d+)""".r
    string match {
      case addPattern(v) => Add(v.toInt)
      case "noop" => NoOp()
    }
  }
}

case class Add(value: Int) extends Instruction {
  override def cycles: Int = 2
}

case class NoOp() extends Instruction {
  override def cycles: Int = 1
}
