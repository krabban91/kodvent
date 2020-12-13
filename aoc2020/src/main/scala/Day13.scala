import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.MathUtils

object Day13 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val start = strings.head.toInt
    val buses = TimeTable.buses(strings(1))
    val time = Range(0,1000000).find(i => buses.exists(_.departs(start+i))).get
    buses.find(_.departs(time+start)).get.id * time
  }

  override def part2(strings: Seq[String]): Long = TimeTable
    .buses(strings(1))
    .foldLeft((1L, 0L))((interval, c) => {
      var t = interval._2
      while (!c.fits(t)) t += interval._1
      (MathUtils.LCM(c.id, interval._1), t)
    })._2

  case class Bus(id: Long, idx: Int) {

    def departs(time: Int): Boolean = time % id == 0

    def fits(time: Long): Boolean = (time + idx) % id == 0

  }

  object TimeTable {
    def buses(s: String): Seq[Bus] = {
      val in = s.split(",")
      in.indices.flatMap(i => maybe(in(i), i))
    }

    private def maybe(s: String, idx: Int): Option[Bus] = {
      if (s == "x") None else Option(Bus(s.toInt, idx))
    }
  }

}
