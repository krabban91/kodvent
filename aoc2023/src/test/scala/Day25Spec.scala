import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day25Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day25.part1TestResult shouldEqual 54
  }
  "Part1" should "be correct" in {
    Day25.part1Result shouldEqual 619225
  }
}
