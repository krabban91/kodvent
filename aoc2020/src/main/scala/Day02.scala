

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(input: scala.Seq[String]): Long = input
    .map(Input(_))
    .map(in => {
      val count = in.pwd.count(_.equals(in.char))
      count >= in.min && count <= in.max
    })
    .count(b => b)

  override def part2(input: scala.Seq[String]): Long = input
    .map(Input(_))
    .map(in => in.pwd(in.min - 1).equals(in.char) ^ in.pwd(in.max - 1).equals(in.char))
    .count(b => b)

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

