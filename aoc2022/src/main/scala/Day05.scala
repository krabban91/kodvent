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
    val map = mutable.HashMap[Int, mutable.Stack[String]]()
    val stackCount = stacks.last.split(" ").last.toInt
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
    val moves = grouped.last.split("\n").filter(_.nonEmpty).map(s => {

      val spl = s.split(" from ")
      val pair = spl.last.split(" to ").map(_.toInt)
      (spl.head.split("move ").last.toInt, (pair.head, pair.last))
    })
    moves.foreach(m => {

      (0 until m._1).foreach(i => {
        if (map.contains(m._2._1)) {
          if (!map.contains(m._2._2)) {
            map.put(m._2._2, mutable.Stack())
          }
          val from = map(m._2._1)
          val to = map(m._2._2)
          if (from.nonEmpty) {
            to.push(from.pop)
          }
        }
      })
    })

    map.keys.toSeq.sorted.map(map(_).head).reduce(_ + _)
  }

  override def part2(strings: Seq[String]): String = {
    val grouped = groupsSeparatedByTwoNewlines(strings)
    val stacks = grouped.head.split("\n").filter(_.nonEmpty)
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
    val moves = grouped.last.split("\n").filter(_.nonEmpty).map(s => {

      val spl = s.split(" from ")
      val pair = spl.last.split(" to ").map(_.toInt)
      (spl.head.split("move ").last.toInt, (pair.head, pair.last))
    })
    moves.foreach(m => {

      val toMove = mutable.Stack[String]()
      (0 until m._1).foreach(i => {
        if (map.contains(m._2._1)) {
          if (!map.contains(m._2._2)) {
            map.put(m._2._2, mutable.Stack())
          }
          val from = map(m._2._1)
          toMove.push(from.pop())
        }
      })
      while (toMove.nonEmpty) {
        val to = map(m._2._2)
        if (toMove.nonEmpty) {
          to.push(toMove.pop)
        }
      }
    })

    map.keys.toSeq.sorted.map(map(_).head).reduce(_+_)
  }

}
