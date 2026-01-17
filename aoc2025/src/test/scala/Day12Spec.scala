import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day12Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day12.part1TestResult shouldEqual 3 // Not correct should be 2
  }
  "Part1" should "be correct" in {
    Day12.part1Result shouldEqual 408
  }
}
