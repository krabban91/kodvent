import aoc.numeric.{AoCPart1, AoCPart2}

import scala.collection.mutable

object Day14 extends App with AoCPart1 with AoCPart2 {

  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val in = strings.map(Instruction(_))
    var currentMask: Mask = null
    val mems = mutable.Map[Long, Long]()
    for (instr <- in) {
      instr match {
        case m: Mask => currentMask = m
        case MemoryUpdate(i, v) => mems(i) = if (currentMask != null) currentMask.adjust(v) else v
      }
    }
    mems.values.sum
  }

  override def part2(strings: Seq[String]): Long = {
    val in = strings.map(Instruction(_))
    var currentMask: Mask = null
    val mems = mutable.Map[Long, Long]()
    for (instr <- in) {
      instr match {
        case m: Mask => currentMask = m
        case MemoryUpdate(i, v) => currentMask.getAddresses(i).foreach(a => mems(a) = v)
      }
    }
    mems.values.sum
  }

  trait Instruction {
  }

  case class Mask(mask: String) extends Instruction {
    def adjust(v: Long): Long = {
      var out = v
      var step = 1L
      for (c <- mask.reverse) {
        if (c == '0') {
          out &= ~step
        }
        if (c == '1') {
          out |= step
        }
        step *= 2
      }
      out
    }

    def getAddresses(address: Long): Seq[Long] = {
      var out = mutable.ListBuffer[Long](address)
      var step = 1L

      for (c <- mask.reverse) {
        if (c == '0') {
        }
        else if (c == '1') {
          out = out.map(_ | step)
        } else {
          out = out.map(_ | step) ++ out.map(_ & ~step)
        }
        step *= 2
      }
      out.toSeq
    }

  }

  case class MemoryUpdate(address: Long, value: Long) extends Instruction {

  }

  object Instruction {
    def apply(string: String): Instruction = {
      if (string.charAt(1) == 'a') {
        Mask(string.split("=")(1).trim)
      } else {
        MemoryUpdate(string.split("]")(0).split("\\[")(1).toInt, string.split("=")(1).trim.toLong)
      }
    }
  }

}
