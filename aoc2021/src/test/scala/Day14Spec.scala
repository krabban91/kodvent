import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day14Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day14.part1TestResult shouldEqual 1588
  }
  "Part1" should "be correct" in {
    Day14.part1Result shouldEqual 2712
  }
  "Part2 Test" should "be correct" in {
    Day14.part2TestResult shouldEqual 2188189693529L
  }
  "Part2" should "be correct" in {
    Day14.part2Result shouldEqual 8336623059567L
  }
}
