import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day16Spec extends AnyFlatSpec with Matchers {
  "Part 1" should "be good for example 1" in {
    Day16.part1(Seq("8A004A801A8002F478")) shouldBe 16
  }
  "Part 1" should "be good for example 2" in {
    Day16.part1(Seq("620080001611562C8802118E34")) shouldBe 12
  }
  "Part 1" should "be good for example 3" in {
    Day16.part1(Seq("C0015000016115A2E0802F182340")) shouldBe 23
  }
  "Part 1" should "be good for example 4" in {
    Day16.part1(Seq("A0016C880162017C3686B18A3D4780")) shouldBe 31
  }

  "Part 2" should "1 + 2" in {
    Day16.part2(Seq("C200B40A82")) shouldBe 3
  }
  "Part 2" should "6 * 9" in {
    Day16.part2(Seq("04005AC33890")) shouldBe 54
  }
  "Part 2" should "min(7,8,9)" in {
    Day16.part2(Seq("880086C3E88112")) shouldBe 7
  }
  "Part 2" should "max(7,8,9)" in {
    Day16.part2(Seq("CE00C43D881120")) shouldBe 9
  }
  "Part 2" should "5 < 15" in {
    Day16.part2(Seq("D8005AC2A8F0")) shouldBe 1
  }
  "Part 2" should "5 > 15" in {
    Day16.part2(Seq("F600BC2D8F")) shouldBe 0
  }
  "Part 2" should "5 == 15" in {
    Day16.part2(Seq("9C005AC2F8F0")) shouldBe 0
  }
  "Part 2" should "1 + 3 == 2 * 2" in {
    Day16.part2(Seq("9C0141080250320F1802104A08")) shouldBe 1
  }

  "Part1" should "be correct" in {
    Day16.part1Result shouldEqual 940
  }

  "Part2" should "be correct" in {
    Day16.part2Result shouldEqual 13476220616073L
  }
}
