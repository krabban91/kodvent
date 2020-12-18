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

  val e0: String = "1 + 2 * 3 + 4 * 5 + 6"
  val e1: String = "1 + (2 * 3) + (4 * (5 + 6))"
  val e2: String = "2 * 3 + (4 * 5)"
  val e3: String = "5 + (8 * 3 + 9 + 3 * 4 * 3)"
  val e4: String = "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"
  val e5: String = "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"
  val e5b: String = "2 * 4 + 2 + ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6)"

  "Expressions part1" should "be evaluated correctly" in {
    Day18.Expression.part1(e0).evaluate shouldEqual  71L
    Day18.Expression.part1(e1).evaluate shouldEqual 51L
    Day18.Expression.part1(e2).evaluate shouldEqual 26L
    Day18.Expression.part1(e3).evaluate shouldEqual 437L
    Day18.Expression.part1(e4).evaluate shouldEqual 12240L
    Day18.Expression.part1(e5).evaluate shouldEqual 13632L
    Day18.Expression.part1(e5b).evaluate shouldEqual 6820
  }
  "Expressions part2" should "be evaluated correctly" in {
    Day18.Expression.part2(e0).evaluate shouldEqual  231L
    Day18.Expression.part2(e1).evaluate shouldEqual 51L
    Day18.Expression.part2(e2).evaluate shouldEqual 46L
    Day18.Expression.part2(e3).evaluate shouldEqual 1445
    Day18.Expression.part2(e4).evaluate shouldEqual 669060
    Day18.Expression.part2(e5).evaluate shouldEqual 23340
    Day18.Expression.part2(e5b).evaluate shouldEqual 23340
  }

  "Expression reconstruction" should "be the same" in {
    Day18.Expression.part1(e5).reconstruct shouldEqual e5
    Day18.Expression.part2(e5).reconstruct shouldEqual e5
    Day18.Expression.part2(Day18.Expression.part1(e0).reconstruct).reconstruct shouldEqual e0
    Day18.Expression.part2(Day18.Expression.part1(e1).reconstruct).reconstruct shouldEqual e1
    Day18.Expression.part2(Day18.Expression.part1(e2).reconstruct).reconstruct shouldEqual e2
    Day18.Expression.part2(Day18.Expression.part1(e3).reconstruct).reconstruct shouldEqual e3
    Day18.Expression.part2(Day18.Expression.part1(e4).reconstruct).reconstruct shouldEqual e4
    Day18.Expression.part2(Day18.Expression.part1(e5).reconstruct).reconstruct shouldEqual e5
    Day18.Expression.part2(Day18.Expression.part1(e5b).reconstruct).reconstruct shouldEqual e5b
  }
}
