import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day09Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day09.part1TestResult shouldEqual 127
  }
  "Part1" should "be correct" in {
    Day09.part1Result shouldEqual 144381670
  }
  "Part2 Test" should "be correct" in {
    Day09.part2TestResult shouldEqual 62
  }
  "Part2" should "be correct" in {
    Day09.part2Result shouldEqual 20532569
  }
}
