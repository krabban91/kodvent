import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day20Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day20.part1TestResult shouldEqual 3
  }
  "Part1" should "be correct" in {
    Day20.part1Result shouldEqual 2622
  }
  "Part2 Test" should "be correct" in {
    Day20.part2TestResult shouldEqual 1623178306
  }
  "Part2" should "be correct" in {
    Day20.part2Result shouldEqual 1538773034088L
  }
}
