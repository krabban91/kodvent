import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day01 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val start = 50
    val instructions = strings.map(s => {
      s.head match {
        case 'L' => (-1, s.tail.toInt)
        case 'R' => (1, s.tail.toInt)
      }

    })
    instructions.foldLeft((start, 0)) { case ((dial, zeros), (dir, ticks)) =>
      val (out, _) = dir match {
        case -1 => rotateLeft(dial, ticks)
        case 1 => rotateRight(dial, ticks)
      }
      (out, zeros + (if (out == 0) 1 else 0))
    }._2

  }

  def rotateLeft(current: Int, ticks: Int, size: Int = 100): (Int, Int) = {
    var zeros = ticks / size
    val left = ticks % size

    val i = current - left
    if (current != 0 && i <= 0) {
      zeros += 1
    }
    ((i + size) % size, zeros)

  }

  def rotateRight(current: Int, ticks: Int, size: Int = 100): (Int, Int) = {
    var zeros = ticks / size
    val left = ticks % size
    val i = current + left
    if (current != 0 && i >= size) {
      zeros += 1
    }
    (i % size, zeros)
  }

  override def part2(strings: Seq[String]): Long = {
    val (start: Int, instructions: Seq[(Int, Int)]) = parse(strings)
    instructions.foldLeft((start, 0)) { case (curr, (dir, ticks)) =>
      val out = dir match {
        case -1 => rotateLeft(curr._1, ticks)
        case 1 => rotateRight(curr._1, ticks)
      }
      (out._1, curr._2 + out._2)
    }._2

  }

  private def parse(strings: Seq[String]) = {
    val start = 50
    val instructions = strings.map(s => {
      s.head match {
        case 'L' => (-1, s.tail.toInt)
        case 'R' => (1, s.tail.toInt)
      }

    })
    (start, instructions)
  }
}
