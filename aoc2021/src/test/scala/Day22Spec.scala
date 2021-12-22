import Day22.Instruction
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day22Spec extends AnyFlatSpec with Matchers {

  behavior of "Part 1"
  it should "Test" in {
    Day22.part1TestResult shouldEqual 474140
  }
  it should "Real" in {
    Day22.part1Result shouldEqual 647076
  }

  behavior of "Part 2"
  it should "Test" in {
    Day22.part2TestResult shouldEqual 2758514936282235L
  }
  it should "Real" in {
    Day22.part2Result shouldEqual 1233304599156793L
  }

  behavior of "Day22.countActive"
  it should "handle multiple overlapping on instructions in 1D" in {
    val ins = Seq(
      Instruction(on = true, (0, 4), (0, 0), (0, 0)),
      Instruction(on = true, (0, 4), (0, 0), (0, 0)),
      Instruction(on = true, (0, 4), (0, 0), (0, 0))
    )
    Day22.countActive(ins) shouldBe 5
  }
  it should "handle off instructions in 1D" in {
    val ins = Seq(
      Instruction(on = true, (0, 4), (0, 0), (0, 0)),
      Instruction(on = false, (0, 4), (0, 0), (0, 0)),
      Instruction(on = true, (0, 3), (0, 0), (0, 0))
    )
    Day22.countActive(ins) shouldBe 4
  }
  it should "handle multiple overlapping on instructions in 2D" in {
    val ins = Seq(
      Instruction(on = true, (0, 4), (0, 1), (0, 0)),
      Instruction(on = true, (0, 4), (0, 1), (0, 0)),
      Instruction(on = true, (0, 4), (0, 0), (0, 0))
    )
    Day22.countActive(ins) shouldBe 10
  }
  it should "handle off instructions in 2D" in {
    val ins = Seq(
      Instruction(on = true, (0, 4), (0, 1), (0, 0)),
      Instruction(on = false, (0, 4), (0, 1), (0, 0)),
      Instruction(on = true, (0, 3), (0, 0), (0, 0))
    )
    Day22.countActive(ins) shouldBe 4
  }
  it should "handle multiple overlapping on instructions in 3D" in {
    val ins = Seq(
      Instruction(on = true, (0, 4), (0, 1), (0, 1)),
      Instruction(on = true, (0, 4), (0, 1), (0, 1)),
      Instruction(on = true, (0, 4), (0, 0), (0, 1))
    )
    Day22.countActive(ins) shouldBe 20
  }
  it should "handle off instructions in 3D" in {
    val ins = Seq(
      Instruction(on = true, (0, 4), (0, 1), (0, 1)),
      Instruction(on = false, (0, 4), (0, 1), (0, 1)),
      Instruction(on = true, (0, 3), (0, 0), (0, 1))
    )
    Day22.countActive(ins) shouldBe 8
  }

}
