import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day19Spec extends AnyFlatSpec with Matchers {
  behavior of "scanner.rotations"
  it should "work" in {
    val (scanner,rest) = Day19.ScanResult("""--- scanner 0 ---
                       |-1,-1,1
                       |-2,-2,2
                       |-3,-3,3
                       |-2,-3,1
                       |5,6,-4
                       |8,0,7""".stripMargin.split("\n"))
    val rotations = scanner.rotations.toSet
    rotations.size shouldBe 20
  }

  behavior of "scanner.overlaps"
  it should "work" in {
    val scanners = Day19.inputs("""--- scanner 0 ---
                                            |0,2
                                            |4,1
                                            |3,3
                                            |
                                            |--- scanner 1 ---
                                            |-1,-1
                                            |-5,0
                                            |-2,1""".stripMargin.split("\n"))
    scanners.head.overlaps(scanners.last, 3) shouldBe true
  }

  behavior of "scanner.findMatch"
  it should "work" in {
    val scanners = Day19.inputs("""--- scanner 0 ---
                                  |0,2
                                  |4,1
                                  |3,3
                                  |
                                  |--- scanner 1 ---
                                  |-1,-1
                                  |-5,0
                                  |-2,1""".stripMargin.split("\n"))
    val maybeResult = scanners.head.findMatch(scanners.last, 3)
    maybeResult.isDefined shouldBe true
    maybeResult.get._1.beacons.toSet shouldBe scanners.head.beacons.toSet
  }


  it should "work for rotated" in {
    val scanners = Day19.inputs("""--- scanner 0 ---
                                  |0,2
                                  |4,1
                                  |3,3
                                  |
                                  |--- scanner 1 ---
                                  |-1,-1
                                  |-5,0
                                  |-2,1""".stripMargin.split("\n"))
    scanners.last.rotations.foreach(o => {
      val maybeResult = scanners.head.findMatch(o._2, 3)
      maybeResult.isDefined shouldBe true
      maybeResult.get._1.beacons.toSet shouldBe scanners.head.beacons.toSet
    })
  }
  it should "1 using 0 in example" in {
    val scanners = Day19.inputs(Day19.getInputTest)
    val maybeResult = scanners.head.findMatch(scanners(1), 12)
    maybeResult.isDefined shouldBe true
    maybeResult.get._1.beacons.toSet.intersect(scanners.head.beacons.toSet).size shouldBe 12
  }

  it should "find 4 using 1 in example" in {
    val scanners = Day19.inputs(Day19.getInputTest)
    val maybeResult = scanners(1).findMatch(scanners(4), 12)
    maybeResult.isDefined shouldBe true
    maybeResult.get._1.beacons.toSet.intersect(scanners(1).beacons.toSet).size shouldBe 12
  }

  behavior of "scanner.diff"
  it should "work" in {
    val scanners = Day19.inputs(Day19.getInputTest)
  }


  behavior of "Part 1"
  it should "Test" in {
    Day19.part1TestResult shouldEqual 79
  }
  it should "Real" in {
    Day19.part1Result shouldEqual 438
  }

  behavior of "Part 2"
  it should "Test" in {
    Day19.part2TestResult shouldEqual -1
  }
  it should "Real" in {
    Day19.part2Result shouldEqual -1
  }
}
