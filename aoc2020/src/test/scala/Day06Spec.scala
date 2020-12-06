import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day06Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day06.part1TestResult shouldEqual 11
  }
  "Part1" should "be correct" in {
    Day06.part1Result shouldEqual 6506
  }
  "Part2 Test" should "be correct" in {
    Day06.part2TestResult shouldEqual 6
  }
  "Part2" should "be correct" in {
    Day06.part2Result shouldEqual 3243
  }
}
