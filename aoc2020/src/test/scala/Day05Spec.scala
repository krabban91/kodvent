import Day05.Boardingpass
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day05Spec extends AnyFlatSpec with Matchers {

  "Part1" should "be correct" in {
    Day05.part1Result shouldEqual 894
  }

  "Part2" should "be correct" in {
    Day05.part2Result shouldEqual 579
  }
  "Boardingpasses" should "not be mutative" in {
    Boardingpass("BBFBFBFLRR").getId shouldEqual "BBFBFBFLRR"
    Boardingpass("BFFBFFFLRR").getId shouldEqual "BFFBFFFLRR"
  }
  "Sanity inputs" should "be valid" in {
    Boardingpass("BFFFBBFRRR").row shouldEqual 70
    Boardingpass("BFFFBBFRRR").column shouldEqual 7
    Boardingpass("BFFFBBFRRR").seatId shouldEqual 567
    Boardingpass("FFFBBBFRRR").row shouldEqual 14
    Boardingpass("FFFBBBFRRR").column shouldEqual 7
    Boardingpass("FFFBBBFRRR").seatId shouldEqual 119
    Boardingpass("BBFFBBFRLL").row shouldEqual 102
    Boardingpass("BBFFBBFRLL").column shouldEqual 4
    Boardingpass("BBFFBBFRLL").seatId shouldEqual 820
  }
}
