import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day02 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1

  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val input: Seq[Seq[String]] = strings.map(_.split(" ").filter(_.nonEmpty).toSeq)
    input.map(par => {
      par.head match {
        case "A" => // rock
          par.tail.head match {
            case "X" => 1 + 3 //rock
            case "Y" => 2 + 6 // paper
            case "Z" => 3 + 0 // scissor
          }
        case "B" => // paper
          par.tail.head match {
            case "X" => 1
            case "Y" => 2 + 3
            case "Z" => 3 + 6
          }
        case "C" => // scissor
          par.tail.head match {
            case "X" => 1 + 6
            case "Y" => 2 + 0
            case "Z" => 3 + 3
          }

      }
    }).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val input: Seq[Seq[String]] = strings.map(_.split(" ").filter(_.nonEmpty).toSeq)
    input.map(par => {
      par.head match {
        case "A" => // rock
          par.tail.head match {
            case "X" => 3 + 0 //lose
            case "Y" => 1 + 3 // draw
            case "Z" => 2 + 6 // win
          }
        case "B" => // paper
          par.tail.head match {
            case "X" => 1 + 0
            case "Y" => 2 + 3
            case "Z" => 3 + 6
          }
        case "C" => // scissor
          par.tail.head match {
            case "X" => 2 + 0
            case "Y" => 3 + 3
            case "Z" => 1 + 6
          }
      }
    }).sum
  }
}
