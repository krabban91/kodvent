import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day03Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day03.part1TestResult shouldEqual 198
  }
  "Part1" should "be correct" in {
    Day03.part1Result shouldEqual 3895776
  }
  "Part2 Test" should "be correct" in {
    Day03.part2TestResult shouldEqual 230
  }
  "Part2" should "be correct" in {
    Day03.part2Result shouldEqual 7928162
  }
}
