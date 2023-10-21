import Day01.groupsSeparatedByTwoNewlines
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day01Spec extends AnyFlatSpec with Matchers {

  "Puzzle" should "be able to place a piece" in {
    val puzzle = Puzzle("xox\no-o\nxox")
    val piece = Piece("x", 0, false)
    val out = puzzle.place(piece)
    puzzle.canPlace(piece) shouldBe true
    out.input shouldBe "xox\noxo\nxox"
  }

  "Puzzle" should "be able to place a piece, example 0" in {
    val input = cleanInput
    val piece = input.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }.filter(_.pieceName == "0U").head
    val puzzle = input.takeRight(1).map(Puzzle).head
    val pos = puzzle.nextPosition
    pos.isDefined shouldBe true
    pos.get shouldBe (1, 1)
    puzzle.canPlace(piece) shouldBe true
    val out = puzzle.place(piece)
    out.input.size shouldBe puzzle.input.size
    out.input shouldBe """
      |oxoxox
      |xoxo-o
      |ox---x
      |xoxo-o
      |o----x
      |xoxoxo""".stripMargin.strip()
  }

  private def cleanInput = {
    groupsSeparatedByTwoNewlines(Day01.getInputTest).filter(_.nonEmpty).map(_.strip())
  }

  private def cleanInputReal = {
    groupsSeparatedByTwoNewlines(Day01.getInput).filter(_.nonEmpty).map(_.strip())
  }

  "Puzzle" should "be able to place a piece, example 1" in {
    val input = cleanInput
    val pieces = input.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }
    val piece0 = pieces.filter(_.pieceName == "0U").head
    val piece1 = pieces.filter(_.pieceName == "1U").head
    val puzzle = input.takeRight(1).map(Puzzle).head.place(piece0)
    val pos = puzzle.nextPosition
    pos.isDefined shouldBe true
    pos.get shouldBe(4, 1)

    puzzle.canPlace(piece1) shouldBe true
    val out = puzzle.place(piece1)
    out.input shouldBe
      """
        |oxoxox
        |xoxoxo
        |oxoxox
        |xoxoxo
        |o---ox
        |xoxoxo""".stripMargin.strip()
    out.input.size shouldBe puzzle.input.size
  }

  "Puzzle" should "be able to place a piece, example 2" in {
    val input = cleanInput
    val pieces = input.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }
    val piece0 = pieces.filter(_.pieceName == "0U").head
    val piece1 = pieces.filter(_.pieceName == "1U").head
    val piece2 = pieces.filter(_.pieceName == "2U").head
    val puzzle = input.takeRight(1).map(Puzzle).head.place(piece0).place(piece1)
    val pos = puzzle.nextPosition
    pos.isDefined shouldBe true
    pos.get shouldBe(1, 4)
    puzzle.canPlace(piece2) shouldBe true
    val out = puzzle.place(piece2)
    out.input.size shouldBe puzzle.input.size
    out.input shouldBe
      """
        |oxoxox
        |xoxoxo
        |oxoxox
        |xoxoxo
        |oxoxox
        |xoxoxo""".stripMargin.strip()
  }

  "Puzzle" should "findNextPiece" in {
    val puzzle = Puzzle("xox\no-o\nxox")
    val out = puzzle.nextPosition
    out.isDefined shouldBe true
    out.contains((1,1)) shouldBe true
  }


  "Piece" should "circle position" in {
    val p = Piece("xox", 2, false)
    p.isCirclePiece shouldBe false
    p.placeBy shouldBe Some((0,0))
  }


  // Validation test pieces
  cleanInput.zipWithIndex.foreach{case (input, i) =>
    val piece = Piece(input = input, index = i, isRotated = false)
    s"Test Piece ${piece.pieceName}" should "have a valid shape" in {
      piece.isValid shouldBe true


    }
  }
  // Validation real pieces
  cleanInputReal.zipWithIndex.foreach { case (input, i) =>
    val piece = Piece(input = input, index = i, isRotated = false)
    s"Real Piece ${piece.pieceName}" should "have a valid shape" in {
      if (!piece.isValid) {
        println(s"piece $piece is invalid")
      }
      piece.isValid shouldBe true

    }
  }



  "Part1 Test" should "be correct" in {
    val solutions = Seq("0U1U2U", "1D2D0D")
    val result = Day01.part1TestResult
    solutions.contains(result) shouldBe true
  }

  "Part1" should "be correct" in {

    Day01.part1Result shouldEqual "-1"
  }
}
