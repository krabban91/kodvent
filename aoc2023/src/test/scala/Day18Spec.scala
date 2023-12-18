import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day18Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day18.part1TestResult shouldEqual 62
  }
  "Part1" should "be correct" in {
    Day18.part1Result shouldEqual 68115
  }
  "Part2 Test" should "be correct" in {
    Day18.part2TestResult shouldEqual 952408144115L
  }
  "Part2" should "be correct" in {
    Day18.part2Result shouldEqual 71262565063800L
  }
}
