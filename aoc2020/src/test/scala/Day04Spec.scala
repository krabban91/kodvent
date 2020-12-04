import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day04Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day04.part1TestResult shouldEqual 2
  }
  "Part1" should "be correct" in {
    Day04.part1Result shouldEqual 200
  }

  "Part2" should "be correct" in {
    Day04.part2Result shouldEqual 116
  }

  "part2" should "find invalid" in {
    Day04.part2(Day04.read("day04/invalid.txt")) shouldEqual 0
  }

  "part2" should "find valid" in {
    Day04.part2(Day04.read("day04/valid.txt")) shouldEqual 4
  }
}
