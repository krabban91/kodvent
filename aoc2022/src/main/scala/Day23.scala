import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.jdk.CollectionConverters.MapHasAsJava

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2


  override def part1(strings: Seq[String]): Long = {
    val initial = Elf.parse(strings)
    val after = (1 to 10).foldLeft((initial, Elf.initialOrder)) { case ((before, orderToUse), i) =>
      Elf.performRound(before, orderToUse)
    }
    Elf.emptySlots(after._1)
  }

  override def part2(strings: Seq[String]): Long = {
    val initial = Elf.parse(strings)
    val arbitrarilyLarge = 100000
    val after = (1 to arbitrarilyLarge).foldLeft((initial, Elf.initialOrder, -1)) { case ((before, orderToUse, res), i) =>
      if (res > 0) {
        (before, orderToUse, res)
      } else {
        if (Elf.noMoves(before)) {
          (before, orderToUse, i)
        } else {
          val (after, nextOrder) = Elf.performRound(before, orderToUse)
          (after, nextOrder, res)
        }
      }
    }
    //logMap(after._1)
    after._3
  }


  private def logMap(elves: Set[Elf]): Unit = {
    val map = elves.map(_.pos).map(p => (p, '#')).toMap
    val java = map.map(kv => (new Point(kv._1._1, kv._1._2), kv._2)).asJava
    println(new LogUtils[Char].mapToText(java, v => if (v == 0) "." else s"$v"))
  }

  case class Elf(pos: (Int, Int)) {

    def noMoves(elfPositions: Set[Elf], check: Map[(Int, Int), Seq[(Int, Int)]]): Boolean = {
      check.values.flatten.map { case (dx, dy) => (pos._1 + dx, pos._2 + dy) }.forall(v => !elfPositions.contains(Elf(v)))
    }

    def suggestMove(allElves: Set[Elf], suggestionOrder: Seq[(Int, Int)]): Elf = {
      if (noMoves(allElves, Elf.check)) {
        this
      } else {
        suggestionOrder
          .find(Elf.check(_).forall { case (dx, dy) => !allElves.contains(Elf(pos._1 + dx, pos._2 + dy)) })
          .map{ case (dx, dy) => (pos._1 + dx, pos._2 + dy)}
          .map(Elf(_))
          .getOrElse(this)
      }
    }
  }

  object Elf {

    val N: (Int, Int) = (0, -1)
    val E: (Int, Int) = (1, 0)
    val S: (Int, Int) = (0, 1)
    val W: (Int, Int) = (-1, 0)

    val NE: (Int, Int) = (1, -1)
    val NW: (Int, Int) = (-1, -1)

    val SE: (Int, Int) = (1, 1)
    val SW: (Int, Int) = (-1, 1)
    val initialOrder: Seq[(Int, Int)] = Seq(N, S, W, E)
    val check: Map[(Int, Int), Seq[(Int, Int)]] = Map(
      N -> Seq(NW, N, NE),
      S -> Seq(SW, S, SE),
      E -> Seq(SE, E, NE),
      W -> Seq(NW, W, SW)
    )


    def parse(strings: Seq[String]): Set[Elf] = {
      strings.zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.filter(_._1 == '#').map { case (c, x) => (x, y) } }
        .map(p => Elf(p))
        .toSet
    }

    def nextSuggestions(order: Seq[(Int, Int)]): Seq[(Int, Int)] = order.tail ++ Set(order.head)

    def noMoves(elves: Set[Elf]): Boolean = elves.forall(_.noMoves(elves, check))

    def performRound(elves: Set[Elf], order: Seq[(Int, Int)]): (Set[Elf], Seq[(Int, Int)]) = {
      val firstHalf = Elf.firstHalf(elves, order)
      val secondHalf = Elf.secondHalf(firstHalf)
      (secondHalf, nextSuggestions(order))
    }

    def firstHalf(elves: Set[Elf], order: Seq[(Int, Int)]): Seq[(Elf, Elf)] = elves.toSeq.map(elf => (elf.suggestMove(elves, order), elf))

    def secondHalf(suggestions: Seq[(Elf, Elf)]): Set[Elf] = {
      suggestions.map{ case (suggested, current) =>
        val collission = suggestions.exists(o => o._1 == suggested && o._2 != current)
        if (collission) {
          current
        } else {
          suggested
        }
      }.toSet
    }

    def emptySlots(elves: Set[Elf]): Long = {
      val map = elves.map(e => (e.pos, e)).toMap
      val minX = map.keySet.map(_._1).min
      val minY = map.keySet.map(_._2).min
      val maxX = map.keySet.map(_._1).max
      val maxY = map.keySet.map(_._2).max
      (minX to maxX).map(x => (minY to maxY).map(y => (x, y)).filterNot(map.contains).size).sum
    }
  }


}
