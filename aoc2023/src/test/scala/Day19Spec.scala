import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day19Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day19.part1TestResult shouldEqual 19114
  }
  "Part1" should "be correct" in {
    Day19.part1Result shouldEqual 346230
  }
  "Part2 Test" should "be correct" in {
    Day19.part2TestResult shouldEqual 167409079868000L
  }
  "Part2" should "be correct" in {
    Day19.part2Result shouldEqual 124693661917133L
  }

  "ratingRange" should "split" in {
    val (l,r) = testRange((50, 100)).test(Day19.Rule("x", 55, false))
    l.isDefined shouldBe true
    l.get.x shouldBe (50, 54)
    r.isDefined shouldBe true
    r.get.x shouldBe (55, 100)
  }
  "ratingRange" should "split2" in {
    val (l, r) = testRange((50, 100)).test(Day19.Rule("x", 105, false))
    l.isDefined shouldBe true
    l.get.x shouldBe(50, 100)
    r.isDefined shouldBe false


  }
  "ratingRange" should "split3" in {
    val (l, r) = testRange((50, 100)).test(Day19.Rule("x", 5, false))
    l.isDefined shouldBe false
    r.isDefined shouldBe true
    r.get.x shouldBe(50, 100)

  }

  "ratingRange" should "splitg" in {
    val (l, r) = testRange((50, 100)).test(Day19.Rule("x", 55, true))
    l.isDefined shouldBe true
    l.get.x shouldBe(56, 100)
    r.isDefined shouldBe true
    r.get.x shouldBe(50, 55)
  }
  "ratingRange" should "splitg2" in {
    val (l, r) = testRange((50, 100)).test(Day19.Rule("x", 105, true))
    l.isDefined shouldBe false
    r.isDefined shouldBe true
    r.get.x shouldBe(50, 100)


  }
  "ratingRange" should "splitg3" in {
    val (l, r) = testRange((50, 100)).test(Day19.Rule("x", 5, true))
    l.isDefined shouldBe true
    l.get.x shouldBe(50, 100)
    r.isDefined shouldBe false
  }

  "ratingRange" should "splitmatch" in {
    val (l, r) = testRange((50, 50)).test(Day19.Rule("x", 49, true))
    l.isDefined shouldBe true
    l.get.x shouldBe(50, 50)
    r.isDefined shouldBe false
  }
  "ratingRange" should "splitmatch2" in {
    val (l, r) = testRange((50, 50)).test(Day19.Rule("x", 51, false))
    l.isDefined shouldBe true
    l.get.x shouldBe(50, 50)
    r.isDefined shouldBe false
  }
  "ratingRange" should "splitmatch1" in {
    val (l, r) = testRange((50, 50)).test(Day19.Rule("x", 51, true))
    r.isDefined shouldBe true
    r.get.x shouldBe(50, 50)
    l.isDefined shouldBe false
  }
  "ratingRange" should "splitmatch3" in {
    val (l, r) = testRange((50, 50)).test(Day19.Rule("x", 49, false))
    r.isDefined shouldBe true
    r.get.x shouldBe(50, 50)
    l.isDefined shouldBe false
  }

  private def testRange(xs: (Long, Long)): Day19.RatingRange = Day19.RatingRange(xs, (1, 1), (1, 1), (1, 1))

}
