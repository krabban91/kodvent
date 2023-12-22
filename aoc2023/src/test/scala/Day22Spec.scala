import Day22.Brick
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day22Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day22.part1TestResult shouldEqual 5
  }
  "Part1" should "be correct" in {
    Day22.part1Result shouldEqual 398
  }
  "Part2 Test" should "be correct" in {
    Day22.part2TestResult shouldEqual 7
  }
  "Part2" should "be correct" in {
    Day22.part2Result shouldEqual 70727
  }

  "Brick" should "have volume" in {
    Brick("2,2,2~2,2,2").volume shouldEqual 1L
    Brick("0,0,10~1,0,10").volume shouldEqual 2L
    Brick("0,0,10~0,1,10").volume shouldEqual 2L
    Brick("0,0,1~0,0,10").volume shouldEqual 10L
  }
}
