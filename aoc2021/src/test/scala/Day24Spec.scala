import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day24Spec extends AnyFlatSpec with Matchers {
  behavior of "Part 1"
  it should "Real" in {
    Day24.part1Result shouldEqual "41299994879959"
  }

  behavior of "Part 2"
  it should "Real" in {
    Day24.part2Result shouldEqual -1
  }
}
