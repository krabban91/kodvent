import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day10Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day10.part1TestResult shouldEqual 35
  }
  "Part1" should "be correct" in {
    Day10.part1Result shouldEqual 1856
  }
  "Part2 Test" should "be correct" in {
    Day10.part2TestResult shouldEqual 8
  }
  "Part2" should "be correct" in {
    Day10.part2Result shouldEqual 2314037239808L
  }
}
