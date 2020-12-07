import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day07Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day07.part1TestResult shouldEqual 4
  }
  "Part1" should "be correct" in {
    Day07.part1Result shouldEqual 265
  }
  "Part2 Test" should "be correct" in {
    Day07.part2TestResult shouldEqual 32
  }
  "Part2" should "be correct" in {
    Day07.part2Result shouldEqual 14177
  }
}
