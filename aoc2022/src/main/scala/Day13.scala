import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.util.parsing.combinator.RegexParsers

object Day13 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    groupsSeparatedByTwoNewlines(strings).map(_.split("\n").filter(_.nonEmpty)
      .map(Packet(_)))
      .zipWithIndex.map { case (l, i) => (i + 1, compare(l.head, l.last)) }
      .map { case (i, i1) => i * (if (i1 >= 0) 1 else 0) }.sum
  }

  def compare(lp: Packet, rp: Packet): Int = {
    (lp, rp) match {
      case (PacketList(left), PacketList(right)) =>
        left.indices.foreach(i => {
          val l = left(i)
          if (right.length <= i) {
            return -1
          }
          val r = right(i)
          (l, r) match {
            case (PacketValue(lv), PacketValue(rv)) =>
              if (rv < lv) return -1
              else if (rv > lv) return 1
            case (PacketList(_), PacketList(_)) =>
              val cmp = compare(l, r)
              if (cmp != 0) {
                return cmp
              }

            case (PacketList(lv), PacketValue(rv)) =>
              val cmp = compare(l, PacketList(Seq(r)))
              if (cmp != 0) {
                return cmp
              }
            case (PacketValue(lv), PacketList(rv)) =>
              val cmp = compare(PacketList(Seq(l)), r)
              if (cmp != 0) {
                return cmp
              }
          }
        })
        if (right.length > left.length) {
          return 1
        }
        0
    }
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  trait Packet {
    def value: Long
  }

  case class PacketList(list: Seq[Packet]) extends Packet {
    override def value: Long = list.map(_.value).sum
  }

  case class PacketValue(value: Long) extends Packet

  object Packet extends RegexParsers {
    def token: Parser[Packet] = value | list

    def value: Parser[Packet] = """\d+""".r.map(_.toLong).map(PacketValue)

    def list: Parser[Packet] = "[" ~ repsep(token, ",") ~ "]" ^^ { case "[" ~ ps ~ "]" => PacketList(ps) }

    def apply(string: String): Packet = parseAll(token, string).get
  }

}
