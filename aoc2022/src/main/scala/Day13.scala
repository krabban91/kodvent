import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.util.parsing.combinator.RegexParsers

object Day13 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = groupsSeparatedByTwoNewlines(strings)
    .map(_.split("\n").filter(_.nonEmpty).map(Packet(_)))
    .map(l => PacketOrdering.compare(l.head, l.last)).zipWithIndex
    .filter(_._1 >= 0)
    .map(_._2 + 1).sum

  override def part2(strings: Seq[String]): Long = {
    val divider = PacketList(Seq(PacketList(Seq(PacketValue(2L)))))
    val divider2 = PacketList(Seq(PacketList(Seq(PacketValue(6L)))))
    val dividers = Seq(divider, divider2)
    val packets = (strings.filter(_.nonEmpty).map(Packet(_)) ++ dividers).sorted.reverse
    dividers.map(packets.indexOf(_) + 1).product
  }

  trait Packet

  case class PacketList(list: Seq[Packet]) extends Packet

  case class PacketValue(value: Long) extends Packet

  object Packet extends RegexParsers {
    def token: Parser[Packet] = value | list

    def value: Parser[Packet] = """\d+""".r.map(_.toLong).map(PacketValue)

    def list: Parser[Packet] = "[" ~ repsep(token, ",") ~ "]" ^^ { case "[" ~ ps ~ "]" => PacketList(ps) }

    def apply(string: String): Packet = parseAll(token, string).get
  }

  implicit object PacketOrdering extends Ordering[Packet] {
    override def compare(x: Packet, y: Packet): Int = (x, y) match {
      case (PacketValue(lv), PacketValue(rv)) =>
        math.signum(rv - lv).toInt
      case (PacketList(left), PacketList(right)) =>
        left.zip(right).map { case (l, r) => compare(l, r) }.find(_ != 0).getOrElse(
          math.signum(right.length - left.length))
      case (l@PacketList(_), r@PacketValue(_)) =>
        compare(l, PacketList(Seq(r)))
      case (l@PacketValue(_), r@PacketList(_)) =>
        compare(PacketList(Seq(l)), r)
    }
  }
}
