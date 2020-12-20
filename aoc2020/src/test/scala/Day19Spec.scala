import Day19.Rule
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable

class Day19Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day19.part1TestResult shouldEqual 2
  }
  "Part1" should "be correct" in {
    Day19.part1Result shouldEqual 113
  }
  "Part2 Test" should "be correct" in {
    Day19.part2TestResult shouldEqual 2
  }
  "Part2" should "be correct" in {
    Day19.part2Result shouldEqual 253
  }

  "matching" should "produce the resulting string of applying pattern" in {
    val str = ("42: 14 42 | 14\n14: \"b\"").split("\n")
    val ma = Day19.Rule.map(str)
    ma(14).matching("ba", ma) shouldEqual Set("a")
    ma(42).matching("ba", ma) shouldEqual Set("a")
    ma(42).matching("bba", ma) shouldEqual Set("a", "ba")
    ma(42).matching("bbba", ma) shouldEqual Set("a", "ba", "bba")
  }

  "bonus" should "be correct" in  {
    val (ruleMap, messages) = Day19.initialData(Day19.read("day19/bonus.txt"))
    messages.count(s => ruleMap(0).fits(s, ruleMap)) shouldEqual 3
    val editable = mutable.Map[Int, Rule]() ++ ruleMap
    editable(8) = Rule("8: 42 | 42 8")
    editable(11) = Rule("11: 42 31 | 42 11 31")
    messages.count(s => ruleMap(0).fits(s, editable.toMap)) shouldEqual 12

  }
}
