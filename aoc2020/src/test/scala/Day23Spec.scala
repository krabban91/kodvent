import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day23Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day23.part1TestResult shouldEqual 67384529
  }
  "Part1" should "be correct" in {
    Day23.part1Result shouldEqual 49576328
  }
  "Part2 Test" should "be correct" in {
    Day23.part2TestResult shouldEqual 149245887792L
  }
  "Part2" should "be correct" in {
    Day23.part2Result shouldEqual 511780369955L
  }
}
