import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day16Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day16.part1TestResult shouldEqual 71
  }
  "Part1" should "be correct" in {
    Day16.part1Result shouldEqual 26009
  }
  "Part2" should "be correct" in {
    Day16.part2Result shouldEqual 589685618167L
  }

  val input: Array[String] = "class: 0-1 or 4-19\nrow: 0-5 or 8-19\nseat: 0-13 or 16-19\n\nyour ticket:\n11,12,13\n\nnearby tickets:\n3,9,18\n15,1,5\n5,14,9".split("\n")

  "input" should "parse data correctly" in {
    val (r, y, t) = Day16.input(input)
    r.size shouldEqual 3
    r.head shouldEqual Day16.Rule("class", Seq((0, 1), (4, 19)))
    y shouldEqual Seq(11, 12, 13)
    t.size shouldEqual 3
    t.head shouldEqual Seq(3, 9, 18)
  }
  "matchingColumns" should "produce a list of possible rule indices" in {
    val (r, y, t) = Day16.input(input)
    val matching = Day16.matchingColumns(r, t)
    matching(r.head) shouldEqual Seq(1, 2)
    matching(r(1)) shouldEqual Seq(0, 1, 2)
    matching(r(2)) shouldEqual Seq(2)
  }

  "viableMatching" should "produce a rule to column mapping" in {
    val (r, y, t) = Day16.input(input)
    val viable = Day16.viableMatching(Day16.matchingColumns(r, t))
    viable.isDefined shouldEqual true
    val v = viable.get
    v(r.head) shouldEqual 1
    v(r(1)) shouldEqual 0
    v(r(2)) shouldEqual 2
  }
}
