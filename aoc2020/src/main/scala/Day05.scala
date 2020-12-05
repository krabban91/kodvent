import aoc.numeric.{AoCPart1, AoCPart2}

object Day05 extends App with AoCPart1 with AoCPart2 {

  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = strings.map(Boardingpass(_)).map(_.seatId).max

  override def part2(strings: Seq[String]): Long = missingSeat(strings.map(Boardingpass(_)).map(_.seatId).sorted)

  def missingSeat(seats: Seq[Long]): Long = seats
    .indices.find(i => i != 0 && seats(i - 1) + 1 != seats(i))
    .map(i => seats(i) - 1)
    .getOrElse(-1)

  object Boardingpass {
    def apply(string: String): Boardingpass = Boardingpass(Integer.parseInt(fromElfBinary(string), 2))

    private def fromElfBinary(string: String) = string
      .replace('F', '0')
      .replace('B', '1')
      .replace('L', '0')
      .replace('R', '1')

  }

  case class Boardingpass(seatId: Long) {

    def row: Long = (seatId - (seatId % 8)) / 8

    def column: Long = (seatId % 8)

    def getId: String = rowInElfBinary + columnInElfBinary

    private def columnInElfBinary: String = ("0" * 3 + column.toBinaryString takeRight 3)
      .replace('0', 'L')
      .replace('1', 'R')

    private def rowInElfBinary: String = ("0" * 7 + row.toBinaryString takeRight 7)
      .replace('0', 'F')
      .replace('1', 'B')
  }

}

