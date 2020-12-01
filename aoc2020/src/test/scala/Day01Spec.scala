import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day01Spec extends AnyFlatSpec with Matchers {
  "Part1" should "be correct" in {
    Day01.part1 shouldEqual 485739
  }
  "Part2" should "be correct" in {
    Day01.part2 shouldEqual 161109702
  }
  "Part1 Test" should "be correct" in {
    Day01.part1Test shouldEqual 514579
  }
  "Part2 Test" should "be correct" in {
    Day01.part2Test shouldEqual 241861950
  }
}
