import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day01Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day01.part1TestResult shouldEqual 7
  }
  "Part1" should "be correct" in {
    Day01.part1Result shouldEqual 1448
  }
  "Part2 Test" should "be correct" in {
    Day01.part2TestResult shouldEqual 5
  }
  "Part2" should "be correct" in {
    Day01.part2Result shouldEqual 1471
  }
}
