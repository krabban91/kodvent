import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day22Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day22.part1TestResult shouldEqual 306
  }
  "Part1" should "be correct" in {
    Day22.part1Result shouldEqual 36257
  }
  "Part2 Test" should "be correct" in {
    Day22.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day22.part2Result shouldEqual -1
  }
}
