import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day11Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day11.part1TestResult shouldEqual 10605
  }
  "Part1" should "be correct" in {
    Day11.part1Result shouldEqual 117640
  }
  "Part2 Test" should "be correct" in {
    Day11.part2TestResult shouldEqual 2713310158L
  }
  "Part2" should "be correct" in {
    Day11.part2Result shouldEqual 30616425600L
  }

  "Monkey" should "be parsed " in {
    val monkey = Day11.Monkey("Monkey 0:\n  Starting items: 22,11\n  Operation: new = old * 11\n  Test: divisible by 13\n    If true: throw to monkey 4\n    If false: throw to monkey 7")
    monkey.items shouldBe Seq(22,11)
  }
}
