import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day15Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day15.part1TestResult shouldEqual 436
  }
  "Part1" should "be correct" in {
    Day15.part1Result shouldEqual 959
  }
  "Part1 bonus input" should "be correct" in {
    Day15.part1(Seq("1,3,2")) shouldEqual 1
    Day15.part1(Seq("2,1,3")) shouldEqual 10
    Day15.part1(Seq("1,2,3")) shouldEqual 27
    Day15.part1(Seq("2,3,1")) shouldEqual 78
    Day15.part1(Seq("3,2,1")) shouldEqual 438
    Day15.part1(Seq("3,1,2")) shouldEqual 1836
  }
  "Part2 Test" should "be correct" in {
    Day15.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day15.part2Result shouldEqual -1
  }
}
