import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day03Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day03.part1TestResult shouldEqual 4361
  }
  "Part1" should "be correct" in {
    Day03.part1Result shouldEqual 544597
  }
  "Part2 Test" should "be correct" in {
    Day03.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day03.part2Result shouldEqual -1
  }
}
