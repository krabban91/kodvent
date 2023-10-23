import Day01.{cleaned, groupsSeparatedByTwoNewlines}
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
    cleaned(Day01.getInputTest)
  }

  private def cleanInputReal = {
    cleaned(Day01.getInput)
  }

  "Puzzle" should "be able to place a piece, example 1" in {
    val input = cleanInput
    val pieces = input.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }
    val piece0 = pieces.filter(_.pieceName == "0U").head
    val piece1 = pieces.filter(_.pieceName == "1U").head
    val piece2 = pieces.filter(_.pieceName == "2U").head

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
    out.hasSolution(Seq(piece2)) shouldBe true
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

  "Piece" should "isRotated circle position" in {
    val p = Piece("xo", 0, false)
    p.isCirclePiece shouldBe false
    p.placeBy shouldBe Some((0, 0))
    p.isRotatedCirclePiece shouldBe true
    p.rotated.isCirclePiece shouldBe true
    p.rotated.placeBy shouldBe Some((0, 0))
  }

  "Piece" should "isRotated circle position 2" in {
    val p = Piece("-x\nxo", 0, false)
    p.isCirclePiece shouldBe false
    p.placeBy shouldBe Some((1, 0))
    p.isRotatedCirclePiece shouldBe true
    p.rotated.isCirclePiece shouldBe true
    p.rotated.placeBy shouldBe Some((0, 0))
  }

  "Piece + puzzle" should "fits shifted" in {
    val p = Piece("-x\nxo", 0, false)
    val puzzle = Puzzle("oxox\nxo-o\no--x\nxoxo")
    p.isCirclePiece shouldBe false
    p.placeBy shouldBe Some((1, 0))
    p.isRotatedCirclePiece shouldBe true
    p.rotated.isCirclePiece shouldBe true
    p.rotated.placeBy shouldBe Some((0, 0))
    puzzle.nextPosition shouldBe Some((2, 1))
    puzzle.canPlace(p) shouldBe true
    puzzle.canPlace(p.rotated) shouldBe false
    val out = puzzle.place(p)
    out.input shouldBe
      """|oxox
         |xoxo
         |oxox
         |xoxo""".stripMargin.strip()
  }
  "Piece + puzzle" should "fits rotated" in {
    val p = Piece("-x\nxo", 0, false)
    val puzzle = Puzzle("oxox\nx--o\no-ox\nxoxo")
    p.isCirclePiece shouldBe false
    p.placeBy shouldBe Some((1, 0))
    p.isRotatedCirclePiece shouldBe true
    p.rotated.isCirclePiece shouldBe true
    p.rotated.placeBy shouldBe Some((0, 0))
    puzzle.nextPosition shouldBe Some((1, 1))
    puzzle.canPlace(p) shouldBe false
    puzzle.canPlace(p.rotated) shouldBe true
    val out = puzzle.place(p.rotated)
    out.input shouldBe
      """|oxox
         |xoxo
         |oxox
         |xoxo""".stripMargin.strip()
  }
  "puzzle piece count" should "for test case be 3" in {
    val input = cleanInput
    val pieces = input.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }
    pieces.size shouldBe 3
  }

  "puzzle piece count" should "for real case be 52" in {
    val input = cleanInputReal
    val pieces = input.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }
    pieces.size shouldBe 53
  }

  "puzzle piece coverage" should "match for test" in {
    val input = cleanInput
    val pieces = input.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }
    val puzzle = input.takeRight(1).map(Puzzle).head
    pieces.map(_.covers.size).sum shouldBe puzzle.unCovered
  }

  "puzzle piece coverage" should "match for real" in {
    val input = cleanInputReal
    val pieces = input.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }
    val puzzle = input.takeRight(1).map(Puzzle).head
    pieces.map(_.covers.size).sum shouldBe puzzle.unCovered
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
    val solutions = Seq(
      "7D3D0D33D39U28U10D5D42U9D36D43U40U12U49U52D50D29U41U2D13D37U45U11U6U21U27D17U31D18D19U20U34U26D24U47U35U46D22U1D51U8U16U25U38U30U44U15U32D4U48U23U14D",
      "14U23D32U25D15D44D48D4D16D30D38D1U24D22D8D51D34D46U47D26U18U31U27D11D45D20D19U35D17D21D37D49D2U29D6D50U13U40D41D42D52U12D28D9U43D36U33U0U3U7U5U10U39D"
    )
    val result = Day01.part1Result
    solutions.contains(result) shouldBe true
  }
}
