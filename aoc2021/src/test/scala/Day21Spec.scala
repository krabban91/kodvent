import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day21Spec extends AnyFlatSpec with Matchers {
  behavior of "Part 1"
  it should "Test" in {
    Day21.part1TestResult shouldEqual 739785
  }
  it should "Real" in {
    Day21.part1Result shouldEqual 512442
  }

  behavior of "Part 2"
  it should "Test" in {
    Day21.part2TestResult shouldEqual 444356092776315L
  }
  it should "Real" in {
    Day21.part2Result shouldEqual 346642902541848L
  }
}
