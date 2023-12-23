import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day23Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day23.part1TestResult shouldEqual -1
  }
  "Part1" should "be correct" in {
    Day23.part1Result shouldEqual -1
  }
  "Part2 Test" should "be correct" in {
    Day23.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day23.part2Result shouldEqual -1
  }

  "Crossings" should "be valid for test" in {
    val input = Day23.getInputTest
    val (map, start, end) = Day23.parse(input)
    val cs = Day23.getCrossings(map, start)
    cs.contains((3L, 5L)) shouldBe true
    cs.contains((5L, 13L)) shouldBe true
    cs.contains((5L+ 8L, 13L)) shouldBe true
  }

  "Distances" should "be calculated for first edge" in {
    val input = Day23.getInputTest
    val (map, start, end) = Day23.parse(input)
    val ds = Day23.distances(map, start, end)
    val targets = ds(start)
    targets.size shouldEqual 1
    targets.head shouldEqual ((3L, 5L), 15L)
  }
}
