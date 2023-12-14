import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day14Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day14.part1TestResult shouldEqual 136
  }
  "Part1" should "be correct" in {
    Day14.part1Result shouldEqual 109665
  }
  "Part2 Test" should "be correct" in {
    Day14.part2TestResult shouldEqual 64
  }
  "Part2" should "be correct" in {
    Day14.part2Result shouldEqual 96061
  }
}
