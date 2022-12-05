import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day05Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day05.part1TestResult shouldEqual "CMZ"
  }
  "Part1" should "be correct" in {
    Day05.part1Result shouldEqual "HBTMTBSDC"
  }
  "Part2 Test" should "be correct" in {
    Day05.part2TestResult shouldEqual "MCD"
  }
  "Part2" should "be correct" in {
    Day05.part2Result shouldEqual "PQTJRSHWS"
  }
}
