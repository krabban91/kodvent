import org.scalatest.FlatSpec

class Day2Spec extends FlatSpec {
  behavior of "Day2"
  it should "part1 yields 51833" in {
    assert(Day2.part1(Day2.getInput())===51833)
  }
  it should "part2 yields 288" in {
    assert(Day2.part2(Day2.getInput())===288)
  }
}


