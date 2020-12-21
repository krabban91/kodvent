import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day21Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day21.part1TestResult shouldEqual 5
  }
  "Part1" should "be correct" in {
    Day21.part1Result shouldEqual 2595
  }
  "Part2 Test" should "be correct" in {
    Day21.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day21.part2Result shouldEqual -1
  }
}
