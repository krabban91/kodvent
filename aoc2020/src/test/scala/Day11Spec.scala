import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day11Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day11.part1TestResult shouldEqual 37
  }
  "Part1" should "be correct" in {
    Day11.part1Result shouldEqual 2152
  }
  "Part2 Test" should "be correct" in {
    Day11.part2TestResult shouldEqual 26
  }
  "Part2" should "be correct" in {
    Day11.part2Result shouldEqual 1937
  }
}
