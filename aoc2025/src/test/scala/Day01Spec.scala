import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day01Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day01.part1TestResult shouldEqual 3
  }
  "Part1" should "be correct" in {
    Day01.part1Result shouldEqual 6
  }
  "Part2 Test" should "be correct" in {
    Day01.part2TestResult shouldEqual 992
  }
  "Part2" should "be correct" in {
    Day01.part2Result shouldEqual 6133
  }
}
