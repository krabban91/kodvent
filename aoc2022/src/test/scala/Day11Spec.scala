import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day11Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day11.part1TestResult shouldEqual 10605
  }
  "Part1" should "be correct" in {
    Day11.part1Result shouldEqual 117640
  }
  "Part2 Test" should "be correct" in {
    Day11.part2TestResult shouldEqual 2713310158L
  }
  "Part2" should "be correct" in {
    Day11.part2Result shouldEqual 30616425600L
  }
}
