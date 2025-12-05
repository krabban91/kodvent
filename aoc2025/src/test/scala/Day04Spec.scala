import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day04Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day04.part1TestResult shouldEqual 13
  }
  "Part1" should "be correct" in {
    Day04.part1Result shouldEqual 1363
  }
  "Part2 Test" should "be correct" in {
    Day04.part2TestResult shouldEqual 43
  }
  "Part2" should "be correct" in {
    Day04.part2Result shouldEqual 8184
  }
}
