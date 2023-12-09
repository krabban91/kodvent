import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day09Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day09.part1TestResult shouldEqual 114
  }
  "Part1" should "be correct" in {
    Day09.part1Result shouldEqual 1853145119
  }
  "Part2 Test" should "be correct" in {
    Day09.part2TestResult shouldEqual 2
  }
  "Part2" should "be correct" in {
    Day09.part2Result shouldEqual 923
  }
}
