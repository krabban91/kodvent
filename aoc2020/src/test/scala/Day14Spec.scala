import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day14Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day14.part1(Seq(
      "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X",
      "mem[8] = 11",
      "mem[7] = 101",
      "mem[8] = 0")) shouldEqual 165
  }
  "Part1" should "be correct" in {
    Day14.part1Result shouldEqual 12408060320841L
  }
  "Part2 Test" should "be correct" in {
    Day14.part2(Seq(
      "mask = 000000000000000000000000000000X1001X",
      "mem[42] = 100",
      "mask = 00000000000000000000000000000000X0XX",
      "mem[26] = 1")) shouldEqual 208
  }
  "Part2" should "be correct" in {
    Day14.part2Result shouldEqual 4466434626828L
  }
}
