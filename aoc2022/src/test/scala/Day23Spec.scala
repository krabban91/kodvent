import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day23Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day23.part1TestResult shouldEqual 110
  }
  "Part1" should "be correct" in {
    Day23.part1Result shouldEqual 4049
  }
  "Part2 Test" should "be correct" in {
    Day23.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day23.part2Result shouldEqual -1
  }
}
