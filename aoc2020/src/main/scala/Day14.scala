import aoc.numeric.{AoCPart1, AoCPart2}

import scala.collection.mutable

object Day14 extends App with AoCPart1 with AoCPart2 {

  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    var mask: Mask = null
    val memory = mutable.Map[Long, Long]()
    strings.map(Instruction(_))
      .foreach {
        case m: Mask => mask = m
        case MemorySet(i, v) => memory(i) = mask.value(v)
      }
    memory.values.sum
  }

  override def part2(strings: Seq[String]): Long = {
    var mask: Mask = null
    val memory = mutable.Map[Long, Long]()
    strings.map(Instruction(_))
      .foreach {
        case m: Mask => mask = m
        case MemorySet(i, v) => mask.addresses(i).foreach(a => memory(a) = v)
      }
    memory.values.sum
  }

  trait Instruction

  case class Mask(mask: String) extends Instruction {
    def value(v: Long): Long = mask
      .reverse
      .foldLeft((v, 1L))((t, c) => (maskValue(t._1, t._2, c), 2 * t._2))
      ._1

    def addresses(address: Long): Seq[Long] = {
      mask
        .reverse
        .foldLeft((Seq(address), 1L))((t, c) => (maskAddress(t._1, t._2, c), 2 * t._2))
        ._1
    }

    private def maskValue(v: Long, step: Long, c: Char): Long = {
      c match {
        case '0' => v & ~step
        case '1' => v | step
        case _ => v
      }
    }

    private def maskAddress(v: Seq[Long], step: Long, c: Char): Seq[Long] = {
      c match {
        case '0' => v
        case '1' => v.map(_ | step)
        case _ => v.map(_ | step) ++ v.map(_ & ~step)
      }
    }
  }

  case class MemorySet(address: Long, value: Long) extends Instruction

  object Instruction {
    def apply(string: String): Instruction = {
      if (string.charAt(1) == 'a') {
        Mask(string.split("=")(1).trim)
      } else {
        MemorySet(string.split("]")(0).split("\\[")(1).toInt, string.split("=")(1).trim.toLong)
      }
    }
  }

}
