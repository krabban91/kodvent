import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DayXXSpec extends AnyFlatSpec with Matchers {
  behavior of "Part 1"
  it should "Test" in {
    DayXX.part1TestResult shouldEqual -1
  }
  it should "Real" in {
    DayXX.part1Result shouldEqual -1
  }

  behavior of "Part 2"
  it should "Test" in {
    DayXX.part2TestResult shouldEqual -1
  }
  it should "Real" in {
    DayXX.part2Result shouldEqual -1
  }
}
