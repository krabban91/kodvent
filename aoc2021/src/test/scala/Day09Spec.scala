import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day09Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day09.part1TestResult shouldEqual 15
  }
  "Part1" should "be correct" in {
    Day09.part1Result shouldEqual 504
  }
  "Part2 Test" should "be correct" in {
    Day09.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day09.part2Result shouldEqual -1
  }
}
