import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day10Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day10.part1TestResult shouldEqual 13140
  }
  "Part1" should "be correct" in {
    Day10.part1Result shouldEqual 16020
  }
  "Part2 Test" should "be correct" in {
    val expected = "\n" +
      "##  ##  ##  ##  ##  ##  ##  ##  ##  ##  \n" +
      "###   ###   ###   ###   ###   ###   ### \n" +
      "####    ####    ####    ####    ####    \n" +
      "#####     #####     #####     #####     \n" +
      "######      ######      ######      ####\n" +
      "#######       #######       #######     "
    Day10.part2TestResult shouldEqual expected
  }
  "Part2" should "be correct" in {
    val expected = "\n" +
      "####  ##  #### #  # ####  ##  #    ###  \n" +
      "#    #  #    # #  #    # #  # #    #  # \n" +
      "###  #      #  #  #   #  #  # #    #  # \n" +
      "#    #     #   #  #  #   #### #    ###  \n" +
      "#    #  # #    #  # #    #  # #    # #  \n" +
      "####  ##  ####  ##  #### #  # #### #  # "
    Day10.part2Result shouldEqual expected
  }
}
