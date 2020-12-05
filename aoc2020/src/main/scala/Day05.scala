import aoc.numeric.{AoCPart1, AoCPart2}

object Day05 extends App with AoCPart1 with AoCPart2 {

  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = strings.map(Boardingpass(_)).map(_.seatId()).max

  override def part2(strings: Seq[String]): Long = {
    val seats = strings.map(Boardingpass(_))
    missingSeat(
      seats
        .filter(b => b.row != seats.minBy(_.row).row && b.row != seats.maxBy(_.row).row)
        .groupBy(_.row)
        .minBy((kv) => kv._2.size)._2)
      .seatId()
  }

  def missingSeat(seats: Seq[Boardingpass]): Boardingpass = {
    val cols = seats.map(_.column).sorted
    var missing = -1
    var prev = -1
    for (i <- cols) {
      if (prev != i - 1) {
        missing = i - 1
      }
      prev = i
    }
    Boardingpass(seats.head.row, missing, "")
  }
}

case class Boardingpass(row: Int, column: Int, raw: String) {
  def seatId(): Long = {
    row * 8 + column
  }

  def getId: String = Boardingpass.toElfBinaryRow("0" * 7 + row.toBinaryString takeRight 7) +
    Boardingpass.toElfBinarySeat("0" * 3 + column.toBinaryString takeRight 3)
}

object Boardingpass {
  def apply(string: String): Boardingpass = Boardingpass(
    Integer.parseInt(fromElfBinary(string.substring(0, 7)), 2),
    Integer.parseInt(fromElfBinary(string.substring(7)), 2), string)

  def apply(seatId: Int): Boardingpass =
    Boardingpass((seatId - (seatId % 8)) / 8, (seatId % 8), "")

  private def fromElfBinary(string: String) = string
    .replace('F', '0')
    .replace('B', '1')
    .replace('L', '0')
    .replace('R', '1')

  private def toElfBinarySeat(string: String) = string
    .replace('0', 'L')
    .replace('1', 'R')

  private def toElfBinaryRow(string: String) = string
    .replace('0', 'F')
    .replace('1', 'B')
}
