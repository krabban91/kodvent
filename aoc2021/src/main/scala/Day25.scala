import aoc.numeric.AoCPart1Test
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day25 extends App with AoCPart1Test {

  printResultPart1Test
  printResultPart1
  private val debug = false

  override def part1(strings: Seq[String]): Long = {
    val (v, maxY, maxX) = parseMap(strings)
    var curr = v
    var noSteps = 1
    logMap(curr, noSteps)
    var next = step(curr, maxX, maxY)
    while (curr != next) {
      curr = next
      logMap(curr, noSteps)
      noSteps += 1
      next = step(curr, maxX, maxY)
    }
    noSteps
  }

  private def parseMap(strings: Seq[String]): (Map[(Int, Int), (Int, Int)], Int, Int) = {
    val v = strings.zipWithIndex.flatMap(ty => ty._1.zipWithIndex.filterNot(_._1 == '.').map(tx => ((tx._2, ty._2), if (tx._1 == 'v') (0, 1) else (1, 0)))).toMap
    val maxY = strings.size - 1
    val maxX = strings.head.length - 1
    (v, maxY, maxX)
  }

  private def step(curr: Map[(Int, Int), (Int, Int)], maxX: Int, maxY: Int): Map[(Int, Int), (Int, Int)] = {
    val next = mutable.HashMap[(Int, Int), (Int, Int)]()
    val preSouth = mutable.HashMap[(Int, Int), (Int, Int)]()
    val east = curr.filter(t => t._2 == (1, 0))
    val south = curr.filter(t => t._2 == (0, 1))

    east.foreach(t => {
      val nt = ((t._1._1 + 1) % (maxX + 1), (t._1._2) % (maxY + 1))
      if (!curr.contains(nt)) {
        preSouth.put(nt, t._2)
        next.put(nt, t._2)
      } else {
        preSouth.put(t._1, t._2)
        next.put(t._1, t._2)
      }
    })
    south.foreach(t => preSouth.put(t._1, t._2))

    south.foreach(t => {
      val nt = ((t._1._1) % (maxX + 1), (t._1._2 + 1) % (maxY + 1))
      if (!preSouth.contains(nt)) {
        next.put(nt, t._2)
      } else {
        next.put(t._1, t._2)
      }
    })

    next.toMap
  }

  private def logMap(curr: Map[(Int, Int), (Int, Int)], noSteps: Int) = if (debug) {
    println(noSteps)
    println(new LogUtils[(Int, Int)]().mapToText(curr.map(t => (new Point(t._1._1, t._1._2), t._2)).asJava, v => if (v == (0, 1)) "v" else if (v == (1, 0)) ">" else "."))
  }

}
