import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Game(id: Long, blues: Int, reds: Int,  greens: Int) {
    def stays(blue: Int, red: Int, green: Int): Boolean = {
      blues<= blue && reds <= red && greens <= green
    }
  }

  object Game{
    def apply(input: String): Game = {
      val v = input.split(":")
      val id = v.head.split(" ").last.strip().toLong
      val rounds = v.last.split(";")
      val counts = rounds.map{ s => val g = s.split(",")
        g.map(_.strip)
          .map(count)
          .reduce(+)
      }.reduce(+)
      Game(id, counts._1, counts._2, counts._3)
    }
  }

  implicit def +(a: (Int, Int, Int), b: (Int, Int, Int)): (Int, Int, Int) = {
    (a._1 + b._1, a._2 + b._2, a._3 + b._3)
  }

  def count(str: String): (Int, Int, Int) = {
    if (str.contains("green")) {
      (0,0, str.split(" ").head.toInt)
    } else if (str.contains("red")) {
      (0, str.split(" ").head.toInt, 0)
    } else if (str.contains("blue")) {
      (str.split(" ").head.toInt, 0,0)
    } else {
      throw new RuntimeException("shouldn't happen")
    }
  }


  override def part1(strings: Seq[String]): Long = {
    val limits = (14, 12, 13)
    strings.map(Game(_))
      .filter(_.stays(14, 12, 13))
      .map(_.id).sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
