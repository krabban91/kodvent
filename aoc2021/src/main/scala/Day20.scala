import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.grid.Grid

import java.awt.Point
import java.util.stream.Collectors
import scala.collection.mutable
import scala.jdk.CollectionConverters._

object Day20 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val imageEnhancer = strings.head.chars().mapToObj(c => if (c == '#') 1 else 0)
      .collect(Collectors.toList[Int]()).asScala
    val initImage: Grid[Int] = new Grid[Int](strings.tail.tail
      .map(s => s.chars().mapToObj(c => if (c == '#') 1 else 0)
        .collect(Collectors.toList[Int]())).toList.asJava)
    var curr = initImage
    for (i <- 1 to 2) {
      curr = curr.expand(p => 0, 10)
      val next = curr
        .map((i, p) => {
          val theNine = curr.getSurroundingTilesWithPoints(p, true)
          val s = theNine.asScala.map(_.getValue.toString).reduce(_ + _)
          imageEnhancer(Integer.parseInt(s, 2))
        })
      curr = next
    }
    val hm = mutable.HashMap[Point, Int]()

    curr.forEach((i, p) => {
      val w = curr.width()
      val h = curr.height()
      if (p.x > 10 && p.x < w - 10 - 1 && p.y > 10 && p.y < h - 10 - 1) {
        hm.put(p, i)
      }
    })
    hm.values.sum
  }

  override def part2(strings: Seq[String]): Long = -1


}
