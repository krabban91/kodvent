import Day21.parse
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day21Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day21.part1TestResult shouldEqual -1
  }
  "Part1" should "be correct" in {
    Day21.part1Result shouldEqual -1
  }
  "Part2 Test" should "be correct" in {
    Day21.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day21.part2Result shouldEqual -1
  }

  "CenterPiece" should "loop as intended" in {
    val input = Day21.getInput
    val (map, start@(sx, sy)) = parse(input)

    Day21.centerLocations(map, (sx, sy), input.head.length, 64) shouldEqual 3649
    Day21.centerLocations(map, (sx, sy), input.head.length, 65) shouldEqual 3832
    Day21.centerLocations(map, (sx, sy), input.head.length, 129) shouldEqual 7520
    (0 to 10 by 2).foreach{ i =>
      Day21.centerLocations(map, (sx, sy), input.head.length, 129 + i) shouldEqual 7520
      Day21.centerLocations(map, (sx, sy), input.head.length, 129 + i + 1) shouldEqual 7457
    }
  }

  "middle-lanes" should "loop as intended" in {
    val input = Day21.getInput
    val (map, start@(sx, sy)) = parse(input)
    val (w, h) = (input.head.length, input.length)
    Day21.middleLocations(map, sx, sy, w, h, sx + 1, 64) shouldEqual Seq(0L)
    Day21.middleLocations(map, sx, sy, w, h, sx + 1, 65) shouldEqual Seq(0L)
    Day21.middleLocations(map, sx, sy, w, h, sx + 1, 66) shouldEqual Seq(1L, 1L, 1L, 1L)
    Day21.middleLocations(map, sx, sy, w, h, sx + 1, 67) shouldEqual Seq(3L, 3L, 3L, 3L)
    (0 to 10 by 2).foreach { i =>
    }
  }

  "corners" should "loop as intended" in {
    val input = Day21.getInput
    val (map, start@(sx, sy)) = parse(input)
    val (w, h) = (input.head.length, input.length)
    //Day21.cornerLocations(map, w, h, sx + 1 + sy + 1, 65 + 65) shouldEqual Seq(0L)
    //Day21.cornerLocations(map, w, h, sx + 1 + sy + 1, 65 + 65 + 1) shouldEqual Seq(0L)
    Day21.cornerLocations(map, w, h, sx + 1 + sy + 1, 65 + 65 + 2) shouldEqual Seq(1L, 1L, 1L, 1L)
    Day21.cornerLocations(map, w, h, sx + 1 + sy + 1, 65 + 65 + 3) shouldEqual Seq(2L, 2L, 2L, 2L)
    Day21.cornerLocations(map, w, h, sx + 1 + sy + 1, 65 + 65 + 4) shouldEqual Seq(4L, 3L, 4L, 3L)
    (0 to 10 by 2).foreach { i =>
    }
  }

}
