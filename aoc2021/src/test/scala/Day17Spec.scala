import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day17Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day17.part1TestResult shouldEqual 45
  }
  "Part1" should "be correct" in {
    Day17.part1Result shouldEqual 11175
  }
  "Part2 Test" should "be correct" in {
    Day17.part2TestResult shouldEqual 112
  }
  "Part2" should "be correct" in {
    Day17.part2Result shouldEqual 3540
  }
}
