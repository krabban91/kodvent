import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day23Spec extends AnyFlatSpec with Matchers {
  behavior of "Part 1"
  it should "Test" in {
    Day23.part1TestResult shouldEqual -1
  }
  it should "Real" in {
    Day23.part1Result shouldEqual -1
  }

  behavior of "Part 2"
  it should "Test" in {
    Day23.part2TestResult shouldEqual -1
  }
  it should "Real" in {
    Day23.part2Result shouldEqual -1
  }
}
