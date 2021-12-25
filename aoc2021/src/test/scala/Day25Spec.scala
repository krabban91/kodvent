import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day25Spec extends AnyFlatSpec with Matchers {
  behavior of "Part 1"
  it should "Test" in {
    Day25.part1TestResult shouldEqual 58
  }
  it should "Real" in {
    Day25.part1Result shouldEqual 579
  }
}
