import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day02Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day02.part1TestResult shouldEqual 1227775554L
  }
  "Part1" should "be correct" in {
    Day02.part1Result shouldEqual 31000881061L
  }
  "Part2 Test" should "be correct" in {
    Day02.part2TestResult shouldEqual 4174379265L
  }
  "Part2" should "be correct" in {
    Day02.part2Result shouldEqual 46769308485L
  }
}
