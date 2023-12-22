import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day16Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day16.part1TestResult shouldEqual 46
  }
  "Part1" should "be correct" in {
    Day16.part1Result shouldEqual 7199
  }
  "Part2 Test" should "be correct" in {
    Day16.part2TestResult shouldEqual 51
  }
  "Part2" should "be correct" in {
    Day16.part2Result shouldEqual 7438
  }
}
