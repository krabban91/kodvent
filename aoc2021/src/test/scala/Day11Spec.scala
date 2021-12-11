import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day11Spec extends AnyFlatSpec with Matchers {
  "stepDay" should "flash correctly" in {
    val d1 = Day11.parseDay("""11111
               |19991
               |19191
               |19991
               |11111""".stripMargin.split("\n"))
    val d2 = Day11.parseDay("""34543
               |40004
               |50005
               |40004
               |34543""".stripMargin.split("\n"))
    Day11.stepDay(d1)._1 shouldBe d2
    Day11.stepDay(d1)._2 shouldBe 9
  }
  "stepDay" should "flash correctly part 1 day 1" in {
    val d1 = Day11.parseDay("""5483143223
                              |2745854711
                              |5264556173
                              |6141336146
                              |6357385478
                              |4167524645
                              |2176841721
                              |6882881134
                              |4846848554
                              |5283751526""".stripMargin.split("\n"))
    val d2 = Day11.parseDay("""6594254334
                              |3856965822
                              |6375667284
                              |7252447257
                              |7468496589
                              |5278635756
                              |3287952832
                              |7993992245
                              |5957959665
                              |6394862637""".stripMargin.split("\n"))
    Day11.stepDay(d1)._2 shouldBe 0
    Day11.stepDay(d1)._1 shouldBe d2
  }

  "stepDay" should "flash correctly part 1 day 2" in {
    val d1 = Day11.parseDay("""6594254334
                              |3856965822
                              |6375667284
                              |7252447257
                              |7468496589
                              |5278635756
                              |3287952832
                              |7993992245
                              |5957959665
                              |6394862637""".stripMargin.split("\n"))
    val d2 = Day11.parseDay("""8807476555
                              |5089087054
                              |8597889608
                              |8485769600
                              |8700908800
                              |6600088989
                              |6800005943
                              |0000007456
                              |9000000876
                              |8700006848""".stripMargin.split("\n"))
    Day11.stepDay(d1)._2 shouldBe 35
    Day11.stepDay(d1)._1 shouldBe d2
  }

  "Part1 Test" should "be correct" in {
    Day11.part1TestResult shouldEqual 1656
  }
  "Part1" should "be correct" in {
    Day11.part1Result shouldEqual 1729
  }
  "Part2 Test" should "be correct" in {
    Day11.part2TestResult shouldEqual 195
  }
  "Part2" should "be correct" in {
    Day11.part2Result shouldEqual 237
  }
}
