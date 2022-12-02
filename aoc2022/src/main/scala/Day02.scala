import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = solve(strings, part2 = false)

  override def part2(strings: Seq[String]): Long = solve(strings, part2 = true)

  private def solve(strings: Seq[String], part2: Boolean): Long = strings
    .map(_.split(" ").filter(_.nonEmpty))
    .map(l => (l.head, l.tail.head))
    .map { case (elf, move) => score(elf, move, part2) }
    .sum


  /**
   * score
   *
   * A: rock 1, B: paper 2, C: scissors 3
   *
   * win: 6, draw: 3, lose: 0
   *
   * part 1
   *
   * X: rock, Y: paper, Z: scissors
   *
   * part 2
   *
   * X: lose, Y: draw, Y: win
   */
  private def score(elf: String, move: String, part2: Boolean): Long = elf match {
    case "A" =>
      move match {
        case "X" => if (part2) 3 + 0 else 1 + 3
        case "Y" => if (part2) 1 + 3 else 2 + 6
        case "Z" => if (part2) 2 + 6 else 3 + 0
      }
    case "B" =>
      move match {
        case "X" => if (part2) 1 + 0 else 1 + 0
        case "Y" => if (part2) 2 + 3 else 2 + 3
        case "Z" => if (part2) 3 + 6 else 3 + 6
      }
    case "C" =>
      move match {
        case "X" => if (part2) 2 + 0 else 1 + 6
        case "Y" => if (part2) 3 + 3 else 2 + 0
        case "Z" => if (part2) 1 + 6 else 3 + 3
      }
  }
}
