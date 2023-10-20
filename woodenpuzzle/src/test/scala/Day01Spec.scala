import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day01Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    val solutions = Seq("1U,2U,3U", "2D,3D,1D")
    val result = Day01.part1TestResult
    solutions.contains(result) shouldBe true
  }
  "Part1" should "be correct" in {
    Day01.part1Result shouldEqual "-1"
  }
}
