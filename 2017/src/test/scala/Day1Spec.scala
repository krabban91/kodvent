import org.scalatest.FlatSpec

class Day1Spec extends FlatSpec {
  behavior of "Day1"
  it should "part1 yields 1203" in {
    assert(Day1.part1(Day1.getInput())===1203)
  }
  it should "part2 yields 1146" in {
    assert(Day1.part2(Day1.getInput())===1146)
  }
}


