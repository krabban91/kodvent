import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day20Spec extends AnyFlatSpec with Matchers {
  behavior of "Part 1"
  it should "Test" in {
    Day20.part1TestResult shouldEqual 35
  }
  it should "Real" in {
    Day20.part1Result shouldEqual 5275
  }

  behavior of "Part 2"
  it should "Test" in {
    Day20.part2TestResult shouldEqual -1
  }
  it should "Real" in {
    Day20.part2Result shouldEqual -1
  }
}
