import aoc.string.{AoCPart1StringTest, AoCPart2StringTest}

import scala.collection.mutable

object Day05 extends App with AoCPart1StringTest with AoCPart2StringTest {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): String = {
    val grouped = groupsSeparatedByTwoNewlines(strings)
    val stacks = grouped.head.split("\n").filter(_.nonEmpty)

    val map: mutable.Map[Int, mutable.Stack[String]] = extractStacks(stacks)

    val moves = grouped.last.split("\n").filter(_.nonEmpty).map(Instruction(_))
    moves.foreach { case Instruction(times, from, to) =>

      (0 until times).foreach(_ => {
        if (map.contains(from)) {
          if (!map.contains(to)) {
            map.put(to, mutable.Stack())
          }
          val fromStack = map(from)
          val toStack = map(to)
          if (fromStack.nonEmpty) {
            toStack.push(fromStack.pop)
          }
        }
      })
    }

    map.keys.toSeq.sorted.map(map(_).head).reduce(_ + _)
  }

  override def part2(strings: Seq[String]): String = {
    val grouped = groupsSeparatedByTwoNewlines(strings)
    val stacks = grouped.head.split("\n").filter(_.nonEmpty)
    val map: mutable.Map[Int, mutable.Stack[String]] = extractStacks(stacks)

    val moves = grouped.last.split("\n").filter(_.nonEmpty).map(Instruction(_))
    moves.foreach { case Instruction(times, from, to) =>

      val toMove = mutable.Stack[String]()
      (0 until times).foreach(_ => {
        if (map.contains(from)) {
          if (!map.contains(to)) {
            map.put(to, mutable.Stack())
          }
          val fromStack = map(from)
          toMove.push(fromStack.pop())
        }
      })
      while (toMove.nonEmpty) {
        val toStack = map(to)
        if (toMove.nonEmpty) {
          toStack.push(toMove.pop)
        }
      }
    }

    map.keys.toSeq.sorted.map(map(_).head).reduce(_ + _)
  }

  private def extractStacks(stacks: Array[String]): mutable.Map[Int, mutable.Stack[String]] = {
    val map = mutable.HashMap[Int, mutable.Stack[String]]()
    stacks.dropRight(1).reverse.foreach(row => {
      val boxes = row.grouped(4).toSeq
      boxes.indices.map(_ + 1).foreach(i => {
        if (!map.contains(i)) {
          map.put(i, mutable.Stack())
        }
        if (boxes(i - 1).startsWith("[")) {
          map(i).push(boxes(i - 1).drop(1).split("]").head)
        }
      })
    })
    map
  }

  case class Instruction(times: Int, from: Int, to: Int) {

  }

  object Instruction {
    def apply(string: String): Instruction = {

      val spl = string.split(" from ")
      val pair = spl.last.split(" to ").map(_.toInt)
      Instruction(spl.head.split("move ").last.toInt, pair.head, pair.last)
    }
  }
}
