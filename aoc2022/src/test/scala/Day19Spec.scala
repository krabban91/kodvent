import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day19Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day19.part1TestResult shouldEqual 33
  }
  "Part1" should "be correct" in {
    Day19.part1Result shouldEqual 1650
  }
  "Part2 Test" should "be correct" in {
    Day19.part2TestResult shouldEqual (56L * 62L)
  }
  "Part2" should "be correct" in {
    Day19.part2Result shouldEqual -1
  }
}
