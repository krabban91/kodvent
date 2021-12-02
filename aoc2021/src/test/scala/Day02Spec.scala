import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day02Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day02.part1TestResult shouldEqual 150
  }
  "Part1" should "be correct" in {
    Day02.part1Result shouldEqual 2322630
  }
  "Part2 Test" should "be correct" in {
    Day02.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day02.part2Result shouldEqual -1
  }
}
