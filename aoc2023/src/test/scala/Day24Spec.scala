import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day24Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day24.part1TestResult shouldEqual -1
  }
  "Part1" should "be correct" in {
    Day24.part1Result shouldEqual -1
  }
  "Part2 Test" should "be correct" in {
    Day24.part2TestResult shouldEqual -1
  }
  "Part2" should "be correct" in {
    Day24.part2Result shouldEqual -1
  }

  "Intersections" should "be found in example 1" in {
    val a = Day24.Hailstorm("19, 13, 30 @ -2, 1, -2")
    val b = Day24.Hailstorm("18, 19, 22 @ -1, -1, -2")
    a.intersection2d(b, (7, 27)) shouldEqual true
  }
  "Intersections" should "be found in example 2" in {
    val a = Day24.Hailstorm("19, 13, 30 @ -2, 1, -2")
    val b = Day24.Hailstorm("20, 25, 34 @ -2, -2, -4")
    a.intersection2d(b, (7, 27)) shouldEqual true
  }
  "Intersections" should "be found in example 3" in {
    val a = Day24.Hailstorm("19, 13, 30 @ -2, 1, -2")
    val b = Day24.Hailstorm("12, 31, 28 @ -1, -2, -1")
    a.intersection2d(b, (7, 27)) shouldEqual false
  }
  "Intersections" should "be found in example 4" in {
    val a = Day24.Hailstorm("19, 13, 30 @ -2, 1, -2")
    val b = Day24.Hailstorm("20, 19, 15 @ 1, -5, -3")
    a.intersection2d(b, (7, 27)) shouldEqual false
  }
  "Intersections" should "be found in example 5" in {
    val a = Day24.Hailstorm("18, 19, 22 @ -1, -1, -2")
    val b = Day24.Hailstorm("20, 25, 34 @ -2, -2, -4")
    a.intersection2d(b, (7, 27)) shouldEqual false
  }
  "Intersections" should "be found in example 6" in {
    val a = Day24.Hailstorm("18, 19, 22 @ -1, -1, -2")
    val b = Day24.Hailstorm("12, 31, 28 @ -1, -2, -1")
    a.intersection2d(b, (7, 27)) shouldEqual false
  }
  "Intersections" should "be found in example 7" in {
    val a = Day24.Hailstorm("18, 19, 22 @ -1, -1, -2")
    val b = Day24.Hailstorm("20, 19, 15 @ 1, -5, -3")
    a.intersection2d(b, (7, 27)) shouldEqual false
  }

  "Intersections" should "be found in example 8" in {
    val a = Day24.Hailstorm("20, 25, 34 @ -2, -2, -4")
    val b = Day24.Hailstorm("12, 31, 28 @ -1, -2, -1")
    a.intersection2d(b, (7, 27)) shouldEqual false
  }
  "Intersections" should "be found in example 9" in {
    val a = Day24.Hailstorm("20, 25, 34 @ -2, -2, -4")
    val b = Day24.Hailstorm("20, 19, 15 @ 1, -5, -3")
    a.intersection2d(b, (7, 27)) shouldEqual false
  }
  "Intersections" should "be found in example 10" in {
    val a = Day24.Hailstorm("12, 31, 28 @ -1, -2, -1")
    val b = Day24.Hailstorm("20, 19, 15 @ 1, -5, -3")
    a.intersection2d(b, (7, 27)) shouldEqual false
  }
  "Intersections" should "be found in parallell byt different velocity" in {
    val a = Day24.Hailstorm("12, 31, 28 @ -1, -2, -1")
    val b = Day24.Hailstorm("5, 24, 21 @ -2, -4, -2")
    a.intersection2d(b, (7, 27)) shouldEqual true
  }

  "derive Vector" should "find first pair in test" in {
    val a = Day24.Hailstorm("20, 19, 15 @ 1, -5, -3")
    val b = Day24.Hailstorm("18, 19, 22 @ -1, -1, -2")
    val start = (24L, 13L, 10L)
    val velocity = (-3L, 1L, 2L)
    val secondHitTime = 3L
    val firstHitTime = 1L
    val expected = (start, velocity, secondHitTime)
    b.deriveCollisionVelocities(a).contains(expected) shouldEqual true
  }
}
