import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day22 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = countActive(strings.map(Instruction(_)).flatMap(_.limited((-50, 50), (-50, 50), (-50, 50))))

  override def part2(strings: Seq[String]): Long = countActive(strings.map(Instruction(_)))

  def countActive(instructions: Seq[Instruction]): Long = instructions
    .foldLeft(Seq[Instruction]())((cubes, instr) => cubes ++ cubes.flatMap(b => instr.intersection(b, !b.on)) ++ (if (instr.on) Seq(instr) else Seq()))
    .map(_.value)
    .sum

  case class Instruction(on: Boolean, xs: (Int, Int), ys: (Int, Int), zs: (Int, Int)) {
    def intersection(other: Instruction, on: Boolean): Option[Instruction] = Option(Instruction(on,
      (math.max(this.xs._1, other.xs._1), math.min(this.xs._2, other.xs._2)),
      (math.max(this.ys._1, other.ys._1), math.min(this.ys._2, other.ys._2)),
      (math.max(this.zs._1, other.zs._1), math.min(this.zs._2, other.zs._2))
    )).filter(_.isValid)

    def value: Long = {
      (if (on) 1 else -1) * (math.abs(this.xs._2 - this.xs._1) + 1).toLong * (math.abs(this.ys._2 - this.ys._1) + 1).toLong * (math.abs(this.zs._2 - this.zs._1).toLong + 1)
    }

    def limited(xs: (Int, Int), ys: (Int, Int), zs: (Int, Int)): Option[Instruction] = {
      intersection(Instruction(on,
        (math.max(this.xs._1, xs._1), math.min(this.xs._2, xs._2)),
        (math.max(this.ys._1, ys._1), math.min(this.ys._2, ys._2)),
        (math.max(this.zs._1, zs._1), math.min(this.zs._2, zs._2))
      ), on)
    }

    private def isValid: Boolean = xs._1 <= xs._2 && ys._1 <= ys._2 && zs._1 <= zs._2
  }

  object Instruction {
    def apply(string: String): Instruction = {
      val s = string.split(" ")
      val on = s.head == "on"
      val ss = s.last.split(",")
      val xs = ss.head.split("=").last.split("\\.\\.").map(_.toInt)
      val ys = ss.tail.head.split("=").last.split("\\.\\.").map(_.toInt)
      val zs = ss.last.split("=").last.split("\\.\\.").map(_.toInt)
      Instruction(on, (xs.head, xs.last), (ys.head, ys.last), (zs.head, zs.last))
    }
  }
}
