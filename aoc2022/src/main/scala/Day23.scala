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
    val N = (0, -1)
    val E = (1, 0)
    val S = (0, 1)
    val W = (-1, 0)

    val NE = (1, -1)
    val NW = (-1, -1)

    val SE = (1, 1)
    val SW = (-1, 1)
    val order = Seq(N, S, W, E)
    val check = Map(
      N -> Seq(NW, N, NE),
      S -> Seq(SW, S, SE),
      E -> Seq(SE, E, NE),
      W -> Seq(NW, W, SW)
    )
    //10 rounds

    var rounds = 10

    val initial = strings.zipWithIndex.flatMap { case (s, y) => s.zipWithIndex.filter(_._1 == '#').map { case (c, x) => (x, y) } }
      .map(p => (p, Elf(p, order)))
      .toMap
    var start = initial
    val after = (1 to 10).foldLeft(initial) { case (before, i) =>
      println(s"before $i")
      logMap(before)

      val firstHalf = before.toSeq.map { case (p@(x, y), elf) =>
        //first half: next location
        val suggestion = elf.suggestMove(before.keySet, check)

        (suggestion, elf)
      }
      val secondHalf = firstHalf.map { case (suggested, current) =>
        val collission = firstHalf.exists(o => o._1.pos == suggested.pos && o._2.pos != current.pos)
        if (collission) {
          val nextSuggestions = current.suggestionOrder.tail ++ Seq(current.suggestionOrder.head)
          val fallBack = Elf(current.pos, nextSuggestions)
          fallBack
        } else {
          suggested
        }
      }
      val value = secondHalf
        .map(elf => (elf.pos, elf))
      value.toMap
    }
    logMap(after)

    val minX = after.keySet.map(_._1).min
    val minY = after.keySet.map(_._2).min
    val maxX = after.keySet.map(_._1).max
    val maxY = after.keySet.map(_._2).max
    (minX to maxX).map(x => (minY to maxY).map(y => (x, y)).filterNot(after.contains).size).sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  private def logMap(elfMap: Map[(Int, Int), Elf]) = {
    val map = elfMap.keySet.map(p => (p, '#')).toMap
    val java = map.map(kv => (new Point(kv._1._1, kv._1._2), kv._2)).asJava
    println(new LogUtils[Char].mapToText(java, v => if (v == 0) "." else s"$v"))
  }

  case class Elf(pos: (Int, Int), suggestionOrder: Seq[(Int, Int)]) {

    def suggestMove(elfPositions: Set[(Int, Int)], check: Map[(Int, Int), Seq[(Int, Int)]]): Elf = {

      val nextSuggestions = suggestionOrder.tail ++ Seq(suggestionOrder.head)
      val fallBack = Elf(this.pos, nextSuggestions)
      if (check.values.flatten.map{case (dx, dy) => (pos._1 + dx, pos._2 + dy)}.forall(v => !elfPositions.contains(v))){
        fallBack
      } else {
        val options = suggestionOrder
          .filter(dir => {
            val value = check(dir)
            val bool = value.forall { case (dx, dy) => !elfPositions.contains((pos._1 + dx, pos._2 + dy)) }
            bool
          })

        val first = options.headOption
        first.map{case p@(dx, dy) =>
          val nextPos = (pos._1 + dx, pos._2 + dy)
          Elf(nextPos, nextSuggestions)
        }.getOrElse(fallBack)
      }
    }

  }


}
