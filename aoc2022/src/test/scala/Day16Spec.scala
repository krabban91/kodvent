import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day16Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day16.part1TestResult shouldEqual 1651
  }
  "Part1" should "be correct" in {
    Day16.part1Result shouldEqual 1775
  }
  "Part2 Test" should "be correct" in {
    Day16.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day16.part2Result shouldEqual -1
  }
}
