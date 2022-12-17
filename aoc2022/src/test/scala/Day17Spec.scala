import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day17Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day17.part1TestResult shouldEqual 3068
  }
  "Part1" should "be correct" in {
    Day17.part1Result shouldEqual 3124
  }
  "Part2 Test" should "be correct" in {
    Day17.part2TestResult shouldEqual 1514285714288L
  }
  "Part2" should "be correct" in {
    Day17.part2Result shouldEqual 1561176470569L
  }
}
