import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day08Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day08.part1TestResult shouldEqual 21
  }
  "Part1" should "be correct" in {
    Day08.part1Result shouldEqual 1803
  }
  "Part2 Test" should "be correct" in {
    Day08.part2TestResult shouldEqual 8
  }
  "Part2" should "be correct" in {
    Day08.part2Result shouldEqual 268912
  }
}
