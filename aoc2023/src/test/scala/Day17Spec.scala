import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day17Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day17.part1TestResult shouldEqual 102
  }
  "Part1" should "be correct" in {
    Day17.part1Result shouldEqual 970
  }
  "Part2 Test" should "be correct" in {
    Day17.part2TestResult shouldEqual 94
  }
  "Part2" should "be correct" in {
    Day17.part2Result shouldEqual 1149
  }
}
