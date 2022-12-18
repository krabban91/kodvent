import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day18Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day18.part1TestResult shouldEqual 64
  }
  "Part1" should "be correct" in {
    Day18.part1Result shouldEqual 4474
  }
  "Part2 Test" should "be correct" in {
    Day18.part2TestResult shouldEqual 58
  }
  "Part2" should "be correct" in {
    Day18.part2Result shouldEqual 2518
  }
}
