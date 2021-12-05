import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day05Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day05.part1TestResult shouldEqual 5
  }
  "Part1" should "be correct" in {
    Day05.part1Result shouldEqual 7436
  }
  "Part2 Test" should "be correct" in {
    Day05.part2TestResult shouldEqual 12
  }
  "Part2" should "be correct" in {
    Day05.part2Result shouldEqual 21104
  }
}
