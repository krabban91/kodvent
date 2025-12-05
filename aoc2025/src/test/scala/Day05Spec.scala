import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day05Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day05.part1TestResult shouldEqual 3
  }
  "Part1" should "be correct" in {
    Day05.part1Result shouldEqual 701
  }
  "Part2 Test" should "be correct" in {
    Day05.part2TestResult shouldEqual 14
  }
  "Part2" should "be correct" in {
    Day05.part2Result shouldEqual 352340558684863L
  }
}
