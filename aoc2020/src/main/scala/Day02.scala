import aoc.numeric.{AoCPart1Test, AoCPart2Test}


object Day02 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(input: Seq[String]): Long = input.map(Input(_))
    .count(in => in.pwd.count(_.equals(in.char)) >= in.min && in.pwd.count(_.equals(in.char)) <= in.max)

  override def part2(input: Seq[String]): Long = input.map(Input(_))
    .count(in => in.pwd(in.min - 1).equals(in.char) ^ in.pwd(in.max - 1).equals(in.char))

  case class Input(pwd: String, min: Int, max: Int, char: Char)

  object Input {
    def apply(in: String): Input = {
      val pwd = in.split(":")(1).trim
      val min = in.split(":")(0).split("-")(0).toInt
      val max = in.split(":")(0).split("-")(1).split(" ")(0).toInt
      val char = in.split(":")(0).split(" ")(1).charAt(0)
      Input(pwd, min, max, char)
    }
  }

}

