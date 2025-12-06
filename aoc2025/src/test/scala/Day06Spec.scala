import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day06Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day06.part1TestResult shouldEqual 4277556
  }
  "Part1" should "be correct" in {
    Day06.part1Result shouldEqual 6343365546996L
  }
  "Part2 Test" should "be correct" in {
    Day06.part2TestResult shouldEqual 3263827
  }
  "Part2" should "be correct" in {
    Day06.part2Result shouldEqual 11136895955912L
  }
}
