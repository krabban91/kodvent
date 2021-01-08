import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day01Spec extends AnyFlatSpec with Matchers {
  "Part1 Test inputs" should "be correct" in {
    Day01.part1(Seq("1122")) shouldEqual 3
    Day01.part1(Seq("1111")) shouldEqual 4
    Day01.part1(Seq("1234")) shouldEqual 0
    Day01.part1(Seq("91212129")) shouldEqual 9
  }
  "Part1" should "be correct" in {
    Day01.part1Result shouldEqual 1203
  }
  "Part2 Test inputs" should "be correct" in {
    Day01.part2(Seq("1212")) shouldEqual 6
    Day01.part2(Seq("1221")) shouldEqual 0
    Day01.part2(Seq("123425")) shouldEqual 4
    Day01.part2(Seq("123123")) shouldEqual 12
    Day01.part2(Seq("12131415")) shouldEqual 4

    Day01.part2(Seq("1122")) shouldEqual 0
    Day01.part2(Seq("1111")) shouldEqual 4
    Day01.part2(Seq("1234")) shouldEqual 0
    Day01.part2(Seq("91212129")) shouldEqual 6
  }
  "Part2" should "be correct" in {
    Day01.part2Result shouldEqual 1146
  }
}
