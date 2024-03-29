import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day24Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day24.part1TestResult shouldEqual 18
  }
  "Part1" should "be correct" in {
    Day24.part1Result shouldEqual 274
  }
  "Part2 Test" should "be correct" in {
    Day24.part2TestResult shouldEqual 54
  }
  "Part2" should "be correct" in {
    Day24.part2Result shouldEqual 839
  }
}
