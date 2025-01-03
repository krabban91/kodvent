import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day12Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day12.part1TestResult shouldEqual 21
  }
  "Part1" should "be correct" in {
    Day12.part1Result shouldEqual 7939
  }
  "Part2 Test" should "be correct" in {
    Day12.part2TestResult shouldEqual 525152
  }
  "Part2" should "be correct" in {
    Day12.part2Result shouldEqual 850504257483930L
  }
}
