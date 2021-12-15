import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day15Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day15.part1TestResult shouldEqual 40
  }
  "Part1" should "be correct" in {
    Day15.part1Result shouldEqual 487
  }
  "Part2 Test" should "be correct" in {
    Day15.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day15.part2Result shouldEqual -1
  }
}
