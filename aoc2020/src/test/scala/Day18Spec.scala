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
    Day18.part2TestResult shouldEqual 231
  }
  "Part2" should "be correct" in {
    Day18.part2Result shouldEqual 4208490449905L
  }

  "Expressions part1" should "be evaluated correctly" in {
    Day18.Expression.part1("1 + 2 * 3 + 4 * 5 + 6").evaluate shouldEqual  71L
    val expression = Day18.Expression.part1("1 + (2 * 3) + (4 * (5 + 6))")
    expression.evaluate shouldEqual 51L
    Day18.Expression.part1("2 * 3 + (4 * 5)").evaluate shouldEqual 26L
    Day18.Expression.part1("5 + (8 * 3 + 9 + 3 * 4 * 3)").evaluate shouldEqual 437L
    Day18.Expression.part1("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))").evaluate shouldEqual 12240L
    Day18.Expression.part1("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2").evaluate shouldEqual 13632L
  }
  "Expressions part2" should "be evaluated correctly" in {
    Day18.Expression.part2("1 + 2 * 3 + 4 * 5 + 6").evaluate shouldEqual  231L
    val expressions = Day18.Expression.part2("1 + (2 * 3) + (4 * (5 + 6))")
    expressions.evaluate shouldEqual 51L
    val expression1 = Day18.Expression.part2("2 * 3 + (4 * 5)")
    expression1.evaluate shouldEqual 46L
    Day18.Expression.part2("5 + (8 * 3 + 9 + 3 * 4 * 3)").evaluate shouldEqual 1445
    Day18.Expression.part2("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))").evaluate shouldEqual 669060
    Day18.Expression.part2("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2").evaluate shouldEqual 23340
    val expression = Day18.Expression.part2("2 * 4 + 2 + ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6)")
    expression.evaluate shouldEqual 23340
  }
}
