import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day03Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day03.part1TestResult shouldEqual 357L
  }
  "Part1" should "be correct" in {
    Day03.part1Result shouldEqual 3121910778619L
  }
  "Part2 Test" should "be correct" in {
    Day03.part2TestResult shouldEqual 16842L
  }
  "Part2" should "be correct" in {
    Day03.part2Result shouldEqual 167523425665348L
  }
}
