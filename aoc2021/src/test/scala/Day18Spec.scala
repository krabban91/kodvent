import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day18Spec extends AnyFlatSpec with Matchers {
  behavior of "Part 1"
  it should "test input" in {
    Day18.part1TestResult shouldEqual -1
  }
  it should "real input" in {
    Day18.part1Result shouldEqual -1
  }
  behavior of "Part 2"
  it should "test input" in {
    Day18.part2TestResult shouldEqual -1
  }
  it should "real input" in {
    Day18.part2Result shouldEqual -1
  }
}
