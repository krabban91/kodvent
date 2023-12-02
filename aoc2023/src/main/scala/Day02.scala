import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.util.parsing.combinator.RegexParsers

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Game(id: Long, blues: Seq[Int], reds: Seq[Int], greens: Seq[Int]) {
    def stays(blue: Int, red: Int, green: Int): Boolean = {
      blues.forall(_ <= blue) && reds.forall(_ <= red) && greens.forall(_ <= green)
    }

    def power(): Long = {
      blues.max.toLong * reds.max.toLong * greens.max.toLong
    }
  }

  object Game {
    def apply(input: String): Game = {
      input match {
        case s"Game $id: $rounds" =>
          val counts = Counts(rounds)
          Game(id.toLong, counts.b, counts.r, counts.g)
      }
    }

    case class Counts(b: Seq[Int], r: Seq[Int], g: Seq[Int]) {
      implicit def ++(b: Counts): Counts = {
        Counts(this.b ++ b.b, this.r ++ b.r, this.g ++ b.g)
      }
    }

    object Counts extends RegexParsers {
      private def blue: Parser[Counts] = """\d+(\.\d*)? blue""".r ^^ { v => Counts(Seq(v.split(" ").head.toInt), Seq(), Seq()) }

      private def red: Parser[Counts] = """(\d+) red""".r ^^ { v => Counts(Seq(), Seq(v.split(" ").head.toInt), Seq()) }

      private def green: Parser[Counts] = """(\d+) green""".r ^^ { v => Counts(Seq(), Seq(), Seq(v.split(" ").head.toInt)) }

      private def toss: Parser[Counts] = blue | green | red

      private def round: Parser[Counts] = toss ~ rep(""", """ ~ log(toss)("round term")) ^^ { case count ~ l => combinator(count, l) }

      private def game: Parser[Counts] = round ~ rep("""; """ ~ log(round)("game term")) ^^ { case count ~ l => combinator(count, l) }

      private def combinator(count: Counts, l: List[String ~ Counts]) = {
        count ++ l.map(_._2).foldLeft(Counts(Seq(), Seq(), Seq())) { case (l, v) => l ++ v }
      }

      def apply(s: String) = {
        parseAll(game, s) match {
          case Success(r, _) => r
          case failure: Failure => print(failure); throw new RuntimeException("bad!")
        }
      }
    }
  }

  implicit def +(a: (Int, Int, Int), b: (Int, Int, Int)): (Int, Int, Int) = {
    (a._1 + b._1, a._2 + b._2, a._3 + b._3)
  }

  def count(str: String): (Int, Int, Int) = {
    if (str.contains("green")) {
      (0, 0, str.split(" ").head.toInt)
    } else if (str.contains("red")) {
      (0, str.split(" ").head.toInt, 0)
    } else if (str.contains("blue")) {
      (str.split(" ").head.toInt, 0, 0)
    } else {
      throw new RuntimeException("shouldn't happen")
    }
  }


  override def part1(strings: Seq[String]): Long = {
    strings.map(Game(_))
      .filter(_.stays(14, 12, 13))
      .map(_.id).sum
  }

  override def part2(strings: Seq[String]): Long = {
    strings.map(Game(_))
      .map(_.power)
      .sum
  }
}
