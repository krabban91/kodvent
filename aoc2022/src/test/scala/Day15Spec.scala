import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day15Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day15.part1TestResult shouldEqual 26
  }
  "Part1" should "be correct" in {
    Day15.part1Result shouldEqual 5878678
  }
  "Part2 Test" should "be correct" in {
    Day15.part2TestResult shouldEqual 56000011
  }
  "Part2" should "be correct" in {
    Day15.part2Result shouldEqual 11796491041245L
  }
}
