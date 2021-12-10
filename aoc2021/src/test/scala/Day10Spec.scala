import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day10Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day10.part1TestResult shouldEqual 26397
  }
  "Part1" should "be correct" in {
    Day10.part1Result shouldEqual 364389
  }
  "Part2 Test" should "be correct" in {
    Day10.part2TestResult shouldEqual 288957
  }
  "Part2" should "be correct" in {
    Day10.part2Result shouldEqual 2870201088L
  }
}
