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

  "Part1" should "be correct" in {
    Day16.part1Result shouldEqual 940
  }

  "Part2" should "be correct" in {
    Day16.part2Result shouldEqual -1
  }
}
