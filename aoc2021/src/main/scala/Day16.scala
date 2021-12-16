import aoc.numeric.{AoCPart1, AoCPart2}

import java.util.stream.Collectors
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.CollectionHasAsScala

object Day16 extends App with AoCPart1 with AoCPart2 {

  printResultPart1
  printResultPart2


  override def part1(strings: Seq[String]): Long = {
    Packet(binary(strings.head)).versionSum
  }


  override def part2(strings: Seq[String]): Long = {
    Packet(binary(strings.head)).result
  }

  trait Packet {
    def version: Int

    def `type`: Int

    def size: Int

    def versionSum: Long

    def result: Long
  }

  case class Operator(version: Int, `type`: Int, packets: Seq[Packet], size: Int) extends Packet {
    override def versionSum: Long = version + packets.map(_.versionSum).sum

    override def result: Long = `type` match {
      case 0 => packets.map(_.result).sum
      case 1 => packets.map(_.result).product
      case 2 => packets.map(_.result).min
      case 3 => packets.map(_.result).max
      case 5 => if (packets.head.result > packets.last.result) 1 else 0
      case 6 => if (packets.head.result < packets.last.result) 1 else 0
      case 7 => if (packets.head.result == packets.last.result) 1 else 0
    }

  }

  case class Literal(version: Int, `type`: Int, value: Long, size: Int) extends Packet {
    override def versionSum: Long = version

    override def result: Long = value
  }

  object Packet {
    def apply(string: String): Packet = {
      val typeId = Integer.parseInt(string.substring(3, 6), 2)
      if (typeId == 4) {
        Literal(string)
      } else {
        Operator(string)
      }
    }
  }

  object Literal {
    def apply(bin: String): Packet = {
      val version = Integer.parseInt(bin.substring(0, 3), 2)
      val typeId = Integer.parseInt(bin.substring(3, 6), 2)
      val (s, _, cost) = bin.substring(6).grouped(5).toSeq
        .foldLeft(("", false, 6))((v, s) => if (v._2) v else (v._1 + s.tail, s.head != '1', v._3 + 5))
      Literal(version, typeId, java.lang.Long.parseLong(s, 2), cost)
    }
  }

  object Operator {
    def apply(bin: String): Packet = {
      val version = Integer.parseInt(bin.substring(0, 3), 2)
      val typeId = Integer.parseInt(bin.substring(3, 6), 2)
      val body = bin.substring(6)
      val lengthTypeId = body.head
      val packetBody = body.tail
      val (packets, cost) = if (lengthTypeId == '0') {
        val lengthInBits = Integer.parseInt(packetBody.substring(0, 15), 2)

        val packets: ListBuffer[Packet] = ListBuffer()
        var inBody = packetBody.substring(15, lengthInBits + 15)
        while (inBody.nonEmpty) {
          val packet = Packet(inBody)
          packets += packet
          inBody = inBody.substring(packet.size)
        }
        (packets.toSeq, 15)
      } else {
        val lengthInPackets = Integer.parseInt(packetBody.substring(0, 11), 2)
        val (packets, _) = (0 until lengthInPackets).foldLeft((Seq[Packet](), packetBody.substring(11)))((t, i) => {
          val packet = Packet(t._2)
          (t._1 ++ Seq(packet), t._2.substring(packet.size))
        })
        (packets, 11)
      }
      Operator(version, typeId, packets, 7 + cost + packets.map(_.size).sum)
    }
  }

  private def binary(string: String): String = {
    val l = string.chars().mapToObj(_.toChar).map(c => binary(c)).collect(Collectors.toList[String]).asScala
    l.reduce(_ + _)
  }

  private def binary(char: Char): String = {
    val i = char match {
      case c if c.isDigit => (c - '0')
      case c if c.isLetter => (c - 'A' + 10)
    }
    val b = i.toBinaryString
    "0".repeat(4 - b.length) + b
  }
}
