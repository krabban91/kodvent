import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day03 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: scala.Seq[String]): Long = countTrees(strings, 3, 1)

  override def part2(strings: scala.Seq[String]): Long = countTrees(strings, 1, 1) *
    countTrees(strings, 3, 1) *
    countTrees(strings, 5, 1) *
    countTrees(strings, 7, 1) *
    countTrees(strings, 1, 2)


  private def countTrees(strings: Seq[String], right: Int, down: Int): Long = {
    var x = 0
    var y = 0
    var trees: Long = 0
    while (y < strings.size) {
      if (strings(y)(x % strings(y).size).equals('#')) trees += 1
      y += down
      x += right

    }
    trees
  }
}
