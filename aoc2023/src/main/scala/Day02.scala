import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.util.parsing.combinator.RegexParsers

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Game(id: Long, blues: Seq[Int], reds: Seq[Int],  greens: Seq[Int]) {
    def stays(blue: Int, red: Int, green: Int): Boolean = {
      blues.forall(_<= blue) && reds.forall(_ <= red) && greens.forall(_ <= green)
    }

    def power(): Long = {
      blues.max.toLong * reds.max.toLong * greens.max.toLong
    }
  }

  case class Counts(b: Seq[Int], r: Seq[Int], g: Seq[Int])

  object Counts extends RegexParsers {
    def blue: Parser[Counts] = """green (\d+)""".r ^^ { v => Counts(Seq(v.toInt), Seq(), Seq()) }
    def red: Parser[Counts] = """red (\d+)""".r ^^ { v => Counts(Seq(), Seq(v.toInt), Seq()) }
    def green: Parser[Counts] = """blue (\d+)""".r ^^ { v => Counts(Seq(), Seq(), Seq(v.toInt)) }
    def toss: Parser[Counts] = green | blue | red
    def round: Parser[Counts] = rep(", " ~ toss) ^^ { l =>l.map(_._2).foldLeft(Counts(Seq(), Seq(), Seq())){case (l, v) => Counts(l.b ++ v.b, l.r++v.r, l.g ++ v.g)} }
    def game: Parser[Counts] = rep("; " ~ round) ^^ { l =>l.map(_._2).foldLeft(Counts(Seq(), Seq(), Seq())){case (l, v) => Counts(l.b ++ v.b, l.r++v.r, l.g ++ v.g)} }

    def apply(s: String) = parseAll(game, s).get
  }

  object Game{
    private val patternGame = """Game (\d+): (.+)""".r

    def apply(input: String): Game = {

      val v = input.split(":")
      val id = v.head.split(" ").last.strip().toLong
      val counts = Counts(v.last)
      Game(id, counts.b, counts.r, counts.g)
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
