import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day08Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day08.part1TestResult shouldEqual 26
  }
  "Part1" should "be correct" in {
    Day08.part1Result shouldEqual 409
  }
  "Part2 Test" should "be correct" in {
    Day08.part2TestResult shouldEqual 61229
  }
  "Part2" should "be correct" in {
    Day08.part2Result shouldEqual 1024649
  }
}
