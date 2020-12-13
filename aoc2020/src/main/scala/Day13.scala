import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.MathUtils

object Day13 extends App with AoCPart1Test with AoCPart2Test {
  //
  //  printResultPart1Test
  //  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val start = strings.head.toInt
    var time = start
    val buses = TimeTable.buses(strings(1))
    while (!buses.exists(_.departs(time))) {
      time += 1
    }
    buses.find(_.departs(time)).get.id * (time - start)
  }

  override def part2(strings: Seq[String]): Long = {
    val buses = TimeTable.buses(strings(1))
    var interval = buses.head.id
    var t = 0L
    buses.tail.foreach(c => {
      while (!c.fits(t)) t += interval
      interval = MathUtils.LCM(c.id, interval)
    })
    t
  }

  case class TimeTable(id: Long, idx: Int) {

    def departs(time: Int): Boolean = time % id == 0

    def fits(time: Long): Boolean = (time + idx) % id == 0

  }

  object TimeTable {
    def buses(s: String): Seq[TimeTable] = {
      val in = s.split(",")
      in.indices.flatMap(i => maybe(in(i), i))
    }

    private def maybe(s: String, idx: Int): Option[TimeTable] = {
      if (s == "x") None else Option(TimeTable(s.toInt, idx))
    }
  }

  def LCM(a: Long, b: Long, idx: Long): Long = {
    a * (b) / GCD(a, b)
  }

  def GCD(a: Long, b: Long): Long = {
    if (b == 0) a else GCD(b, a % b)
  }

}
