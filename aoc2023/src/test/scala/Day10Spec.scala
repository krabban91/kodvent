import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day10Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day10.part1TestResult shouldEqual 80
  }
  "Part1" should "be correct" in {
    Day10.part1Result shouldEqual 6951
  }
  "Part2 Test" should "be correct" in {
    Day10.part2TestResult shouldEqual 10
  }
  "Part2" should "be correct" in {
    Day10.part2Result shouldEqual 563
  }
}
