import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day13Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day13.part1TestResult shouldEqual 295
  }
  "Part1" should "be correct" in {
    Day13.part1Result shouldEqual 4808
  }
  "Part2 Test" should "be correct" in {
    Day13.part2TestResult shouldEqual 1068781
  }
  "Part2" should "be correct" in {
    Day13.part2Result shouldEqual 741745043105674L
  }

  "Part2 bonus input" should "be correct" in {
    Day13.part2(Seq("1","17,x,13,19")) shouldEqual 3417
    Day13.part2(Seq("1","67,7,59,61")) shouldEqual 754018
    Day13.part2(Seq("1","67,x,7,59,61")) shouldEqual 779210
    Day13.part2(Seq("1","67,7,x,59,61")) shouldEqual 1261476
    Day13.part2(Seq("1","1789,37,47,1889")) shouldEqual 1202161486
  }
}
