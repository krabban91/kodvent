import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day13Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day13.part1TestResult shouldEqual 405
  }
  "Part1" should "be correct" in {
    Day13.part1Result shouldEqual 34202
  }
  "Part2 Test" should "be correct" in {
    Day13.part2TestResult shouldEqual 400
  }
  "Part2" should "be correct" in {
    Day13.part2Result shouldEqual 34230
  }
}
