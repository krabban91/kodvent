import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day09Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day09.part1TestResult shouldEqual 88
  }
  "Part1" should "be correct" in {
    Day09.part1Result shouldEqual 5735
  }
  "Part2 Test" should "be correct" in {
    Day09.part2TestResult shouldEqual 36
  }
  "Part2" should "be correct" in {
    Day09.part2Result shouldEqual 2478
  }
}
