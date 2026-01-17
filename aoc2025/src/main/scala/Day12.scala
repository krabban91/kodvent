import aoc.numeric.AoCPart1Test

object Day12 extends App with AoCPart1Test {

  printResultPart1Test
  printResultPart1

  case class Piece(id: Int, covers: Set[(Int, Int)]) {
    def area: Int = covers.size
  }

  case class Puzzle(width: Int, height: Int, pieces: Map[Int, Int]) {
    def area: Int = width * height

    def fits(pieces: Seq[Piece]): Boolean = {
      val estimate = this.pieces.map{ case (i, count) => pieces(i).area*count}.sum
      estimate <= area
    }
  }

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
    puzzles.count(_.fits(pieces)).toLong
  }

}
