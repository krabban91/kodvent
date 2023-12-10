import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day08Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day08.part1TestResult shouldEqual 2L
  }
  "Part1" should "be correct" in {
    Day08.part1Result shouldEqual 20093L
  }
  "Part2 Test" should "be correct" in {
    Day08.part2TestResult shouldEqual 6L
  }
  "Part2" should "be correct" in {
    Day08.part2Result shouldEqual 22103062509257L
  }
}
