import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day16Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day16.part1TestResult shouldEqual 71
  }
  "Part1" should "be correct" in {
    Day16.part1Result shouldEqual 26009
  }
  "Part2" should "be correct" in {
    Day16.part2Result shouldEqual 589685618167L
  }
}
