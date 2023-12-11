import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day11Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day11.part1TestResult shouldEqual 374
  }
  "Part1" should "be correct" in {
    Day11.part1Result shouldEqual 10077850
  }
  "Part2 Test" should "be correct" in {
    Day11.part2TestResult shouldEqual 82000210
  }
  "Part2" should "be correct" in {
    Day11.part2Result shouldEqual 504715068438L
  }
}
