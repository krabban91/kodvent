import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day15 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class HASH(str: String) {
    def value: Long = {
      calculate(str)
    }

    def calculate(v: String): Long = {
      val out = v.foldLeft(0L) { case (o, h) =>
        val inc = o + h.toLong
        val mul = inc * 17
        val byte = mul % 256
        byte
      }

      out
    }
    def labelString: String = {
      str.split("=").head.split("-").head
    }

    def operation: (Boolean, Long, Long) = {
      if (str.contains("=")) {
        // =
        val s = str.split("=")
        (true, calculate(s.head), s.last.toLong)
      } else {
        // -
        val s = str.split("\\-")
        (false, calculate(s.head), 0)
      }
    }

    def label: Long = operation._2

    def focalLength: Long = operation._3
  }

  override def part1(strings: Seq[String]): Long = {
    strings.head.split(",").map(HASH).toSeq.map(_.value).map(_.toLong).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val hashMap = mutable.HashMap[Long, Seq[HASH]]()
    strings.head.split(",").map(HASH).toSeq.foreach { h =>
      val op = h.operation
      val loc = h.label
      if (op._1) {

        val curr = hashMap.getOrElse(loc, Seq[HASH]())
        if (!curr.exists(_.labelString == h.labelString)) {
          hashMap.put(loc, curr.filterNot(_.labelString == h.labelString) ++ Seq(h))
        } else {
          val before = curr.takeWhile(_.labelString != h.labelString)
          val after = curr.dropWhile(_.labelString != h.labelString).drop(1)
          hashMap.put(loc,before ++ Seq(h) ++ after)
        }
      } else {
        hashMap.put(loc, hashMap.getOrElse(loc, Seq[HASH]()).filterNot(_.labelString == h.labelString))
      }
    }
    hashMap.map { case (i, lenses) => lenses.zipWithIndex
        .map { case (h, ix) =>
          val length = h.focalLength
          val l = (i + 1) * (ix + 1) * length
          l
        }.sum
      }.sum
  }
}
