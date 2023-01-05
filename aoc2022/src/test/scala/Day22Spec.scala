import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day22Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day22.part1TestResult shouldEqual 6032
  }
  "Part1" should "be correct" in {
    Day22.part1Result shouldEqual 126350
  }
  "Part2 Test" should "be correct" in {
    Day22.part2TestResult shouldEqual 5031
  }
  "Part2" should "be correct" in {
    Day22.part2Result shouldEqual 129339
  }

  "passOverEdge" should "rotate" in {
    Day22.passOverEdge((3, -1), 4, ('N', 'N')) shouldBe (3, 3)
    Day22.passOverEdge((2, -1), 4, ('N', 'N')) shouldBe (2, 3)
    Day22.passOverEdge((1, -1), 4, ('N', 'N')) shouldBe (1, 3)
    Day22.passOverEdge((0, -1), 4, ('N', 'N')) shouldBe (0, 3)
    Day22.passOverEdge((3, -1), 4, ('N', 'W')) shouldBe (3, 0)
    Day22.passOverEdge((2, -1), 4, ('N', 'W')) shouldBe (3, 1)
    Day22.passOverEdge((1, -1), 4, ('N', 'W')) shouldBe (3, 2)
    Day22.passOverEdge((0, -1), 4, ('N', 'W')) shouldBe (3, 3)
    Day22.passOverEdge((3, -1), 4, ('N', 'E')) shouldBe (0, 3)
    Day22.passOverEdge((2, -1), 4, ('N', 'E')) shouldBe (0, 2)
    Day22.passOverEdge((1, -1), 4, ('N', 'E')) shouldBe (0, 1)
    Day22.passOverEdge((0, -1), 4, ('N', 'E')) shouldBe (0, 0)
    Day22.passOverEdge((3, -1), 4, ('N', 'S')) shouldBe (0, 0)
    Day22.passOverEdge((2, -1), 4, ('N', 'S')) shouldBe (1, 0)
    Day22.passOverEdge((1, -1), 4, ('N', 'S')) shouldBe (2, 0)
    Day22.passOverEdge((0, -1), 4, ('N', 'S')) shouldBe (3, 0)

    Day22.passOverEdge((3, 4), 4, ('S', 'S')) shouldBe (3, 0)
    Day22.passOverEdge((2, 4), 4, ('S', 'S')) shouldBe (2, 0)
    Day22.passOverEdge((1, 4), 4, ('S', 'S')) shouldBe (1, 0)
    Day22.passOverEdge((0, 4), 4, ('S', 'S')) shouldBe (0, 0)
    Day22.passOverEdge((3, 4), 4, ('S', 'W')) shouldBe (3, 3)
    Day22.passOverEdge((2, 4), 4, ('S', 'W')) shouldBe (3, 2)
    Day22.passOverEdge((1, 4), 4, ('S', 'W')) shouldBe (3, 1)
    Day22.passOverEdge((0, 4), 4, ('S', 'W')) shouldBe (3, 0)
    Day22.passOverEdge((3, 4), 4, ('S', 'E')) shouldBe (0, 0)
    Day22.passOverEdge((2, 4), 4, ('S', 'E')) shouldBe (0, 1)
    Day22.passOverEdge((1, 4), 4, ('S', 'E')) shouldBe (0, 2)
    Day22.passOverEdge((0, 4), 4, ('S', 'E')) shouldBe (0, 3)
    Day22.passOverEdge((3, 4), 4, ('S', 'N')) shouldBe (0, 3)
    Day22.passOverEdge((2, 4), 4, ('S', 'N')) shouldBe (1, 3)
    Day22.passOverEdge((1, 4), 4, ('S', 'N')) shouldBe (2, 3)
    Day22.passOverEdge((0, 4), 4, ('S', 'N')) shouldBe (3, 3)

    Day22.passOverEdge((4, 0), 4, ('E', 'E')) shouldBe (0, 0)
    Day22.passOverEdge((4, 1), 4, ('E', 'E')) shouldBe (0, 1)
    Day22.passOverEdge((4, 2), 4, ('E', 'E')) shouldBe (0, 2)
    Day22.passOverEdge((4, 3), 4, ('E', 'E')) shouldBe (0, 3)
    Day22.passOverEdge((4, 0), 4, ('E', 'W')) shouldBe (3, 3)
    Day22.passOverEdge((4, 1), 4, ('E', 'W')) shouldBe (3, 2)
    Day22.passOverEdge((4, 2), 4, ('E', 'W')) shouldBe (3, 1)
    Day22.passOverEdge((4, 3), 4, ('E', 'W')) shouldBe (3, 0)
    Day22.passOverEdge((4, 0), 4, ('E', 'N')) shouldBe (0, 3)
    Day22.passOverEdge((4, 1), 4, ('E', 'N')) shouldBe (1, 3)
    Day22.passOverEdge((4, 2), 4, ('E', 'N')) shouldBe (2, 3)
    Day22.passOverEdge((4, 3), 4, ('E', 'N')) shouldBe (3, 3)
    Day22.passOverEdge((4, 0), 4, ('E', 'S')) shouldBe (3, 0)
    Day22.passOverEdge((4, 1), 4, ('E', 'S')) shouldBe (2, 0)
    Day22.passOverEdge((4, 2), 4, ('E', 'S')) shouldBe (1, 0)
    Day22.passOverEdge((4, 3), 4, ('E', 'S')) shouldBe (0, 0)

    Day22.passOverEdge((-1, 0), 4, ('W', 'S')) shouldBe (0, 0)
    Day22.passOverEdge((-1, 1), 4, ('W', 'S')) shouldBe (1, 0)
    Day22.passOverEdge((-1, 2), 4, ('W', 'S')) shouldBe (2, 0)
    Day22.passOverEdge((-1, 3), 4, ('W', 'S')) shouldBe (3, 0)
    Day22.passOverEdge((-1, 0), 4, ('W', 'W')) shouldBe (3, 0)
    Day22.passOverEdge((-1, 1), 4, ('W', 'W')) shouldBe (3, 1)
    Day22.passOverEdge((-1, 2), 4, ('W', 'W')) shouldBe (3, 2)
    Day22.passOverEdge((-1, 3), 4, ('W', 'W')) shouldBe (3, 3)
    Day22.passOverEdge((-1, 0), 4, ('W', 'E')) shouldBe (0, 3)
    Day22.passOverEdge((-1, 1), 4, ('W', 'E')) shouldBe (0, 2)
    Day22.passOverEdge((-1, 2), 4, ('W', 'E')) shouldBe (0, 1)
    Day22.passOverEdge((-1, 3), 4, ('W', 'E')) shouldBe (0, 0)
    Day22.passOverEdge((-1, 0), 4, ('W', 'N')) shouldBe (3, 3)
    Day22.passOverEdge((-1, 1), 4, ('W', 'N')) shouldBe (2, 3)
    Day22.passOverEdge((-1, 2), 4, ('W', 'N')) shouldBe (1, 3)
    Day22.passOverEdge((-1, 3), 4, ('W', 'N')) shouldBe (0, 3)
  }
}
