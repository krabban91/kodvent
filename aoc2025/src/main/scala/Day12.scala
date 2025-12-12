import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day12 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Piece(id: Int, covers: Set[(Int, Int)])

  case class Puzzle(width: Int, height: Int, pieces: Map[Int, Int])

  object Piece {
    def apply(str: String): Piece = {
      val lines = str.split("\n")
      val id = lines.head.stripSuffix(":").toInt
      val covers = lines.tail.zipWithIndex.flatMap{ case (str, y) => str.zipWithIndex.filter(_._1 == '#').map{case (_, x) => (x, y)}}.toSet
      Piece(id, covers)
    }
  }

  object Puzzle {
    def apply(str: String): Puzzle = {
      val strs = str.strip().split(":")
      val dims = strs.head.split("x")
      val (w, h) = (dims.head.toInt, dims.last.toInt)
      val needs = strs.last.strip().split(" ").zipWithIndex.map{ case (str, i) => (i, str.toInt)}.toMap
      Puzzle(w, h, needs)
    }
  }

  private def readInput(value: Seq[String]): (Seq[Piece], Seq[Puzzle]) = {
    val groups = groupsSeparatedByTwoNewlines(value).map(_.strip()).filter(_.nonEmpty)
    val pieces = groups.dropRight(1).map(Piece(_))
    val puzzles = groups.last.split("\n").map(Puzzle(_)).toSeq
    (pieces, puzzles)
  }

  override def part1(strings: Seq[String]): Long = {
    val (pieces, puzzles) = readInput(strings)
    -1
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }
}
