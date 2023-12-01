import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day01Spec extends AnyFlatSpec with Matchers {
  "Part1 example" should "be correct" in {
    val example =
      """
        |1abc2
        |pqr3stu8vwx
        |a1b2c3d4e5f
        |treb7uchet
        |""".stripMargin.split("\n")
    Day01.part1(example) shouldEqual 142
  }
  "Part1 Test" should "be correct" in {
    Day01.part1TestResult shouldEqual 209
  }
  "Part1" should "be correct" in {
    Day01.part1Result shouldEqual 54953
  }
  "Part2 Test" should "be correct" in {
    Day01.part2TestResult shouldEqual 281
  }
  "Part2" should "be correct" in {
    Day01.part2Result shouldEqual 53868
  }
}
