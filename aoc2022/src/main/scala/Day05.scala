import aoc.string.{AoCPart1StringTest, AoCPart2StringTest}

import scala.collection.mutable

object Day05 extends App with AoCPart1StringTest with AoCPart2StringTest {

  override def part1(strings: Seq[String]): String = {
    val (stacks, instructions) = parseInput(strings)
    instructions.foreach(_.run1(stacks))
    topLayer(stacks).reduce(_ + _)
  }


  override def part2(strings: Seq[String]): String = {
    val (stacks, instructions) = parseInput(strings)
    instructions.foreach(_.run2(stacks))
    topLayer(stacks).reduce(_ + _)
  }

  private def parseInput(strings: Seq[String]): (mutable.Map[Int, mutable.Stack[String]], Array[Instruction]) = {
    val grouped = groupsSeparatedByTwoNewlines(strings)
    val stacks = extractStacks(grouped.head.split("\n").filter(_.nonEmpty))
    val moves = grouped.last.split("\n").filter(_.nonEmpty).map(Instruction(_))
    (stacks, moves)
  }

  private def extractStacks(stacks: Array[String]): mutable.Map[Int, mutable.Stack[String]] = {
    val map = mutable.HashMap[Int, mutable.Stack[String]]()
    val count = stacks.last.split(" ").last.toInt
    (1 to count).foreach(i => map.put(i, mutable.Stack()))
    stacks.dropRight(1).reverse.foreach(row => {
      val boxes = row.grouped(4).toSeq
      boxes.indices.foreach(i => {
        if (boxes(i).startsWith("[")) {
          map(i + 1).push(boxes(i).drop(1).split("]").head)
        }
      })
    })
    map
  }

  private def topLayer(stacks: mutable.Map[Int, mutable.Stack[String]]): Seq[String] = stacks.keys.toSeq.sorted.map(stacks(_).head)

  case class Instruction(times: Int, from: Int, to: Int) {
    def run1(stacks: mutable.Map[Int, mutable.Stack[String]]): Unit = {
      (0 until times).foreach(_ => {
        val fromStack = stacks(from)
        val toStack = stacks(to)
        toStack.push(fromStack.pop)
      })
    }

    def run2(stacks: mutable.Map[Int, mutable.Stack[String]]): Unit = {
      val toMove = mutable.Stack[String]()
      (0 until times).foreach(_ => {
        val fromStack = stacks(from)
        toMove.push(fromStack.pop())
      })
      while (toMove.nonEmpty) {
        val toStack = stacks(to)
        toStack.push(toMove.pop)
      }
    }
  }

  object Instruction {
    private val pattern = """move (\d+) from (\d+) to (\d+)""".r

    def apply(string: String): Instruction = string match {
      case pattern(a, b, c) => Instruction(a.toInt, b.toInt, c.toInt)
    }
  }
}
