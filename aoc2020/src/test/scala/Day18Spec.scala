import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day18Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day18.part1TestResult shouldEqual 71
  }
  "Part1" should "be correct" in {
    Day18.part1Result shouldEqual 701339185745L
  }
  "Part2 Test" should "be correct" in {
    Day18.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day18.part2Result shouldEqual -1
  }

  "Expressions rightToLeft" should "be evaluated correctly" in {
    Day18.Expression("1 + 2 * 3 + 4 * 5 + 6", true).evaluate shouldEqual  71L
    val expression = Day18.Expression("1 + (2 * 3) + (4 * (5 + 6))", true)
    expression.evaluate shouldEqual 51L
    Day18.Expression("2 * 3 + (4 * 5)", true).evaluate shouldEqual 26L
    Day18.Expression("5 + (8 * 3 + 9 + 3 * 4 * 3)", true).evaluate shouldEqual 437L
    Day18.Expression("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", true).evaluate shouldEqual 12240L
    Day18.Expression("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", true).evaluate shouldEqual 13632L
  }
}
