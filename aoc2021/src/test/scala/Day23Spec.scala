import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day23Spec extends AnyFlatSpec with Matchers {
  behavior of "Part 1"
  it should "Test" in {
    Day23.part1TestResult shouldEqual 12521
  }
  it should "Real" in {
    Day23.part1Result shouldEqual 17120
  }

  behavior of "Part 2"
  it should "Test" in {
    Day23.part2TestResult shouldEqual 44169
  }
  it should "Real" in {
    Day23.part2Result shouldEqual 47234
  }
}
