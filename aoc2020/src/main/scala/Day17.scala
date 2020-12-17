import java.util.stream.Collectors

import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.{LogUtils, Loggable}
import krabban91.kodvent.kodvent.utilities.{Point3D, TimePoint}
import krabban91.kodvent.kodvent.utilities.grid.{CubeGrid, HyperCubeGrid}

import scala.jdk.CollectionConverters._

object Day17 extends App with AoCPart1Test with AoCPart2Test {

  val debug = false

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    var curr: CubeGrid[Conway] = new CubeGrid[Conway](java.util.List.of(strings.map(s => s.chars()
      .mapToObj(c => Conway(c == '#'))
      .collect(Collectors.toList[Conway]))
      .asJava))
    for (i <- Range(0, 6)) {
      if (debug) {
        println(s"iteration: $i.")
        logCube(curr)
      }
      curr = curr.expandOneStep(Conway(_))
      curr = curr.map((c, p) => c.cycle(curr.getSurroundingTiles(p).asScala))
    }
    curr.sum(c => if (c.active) 1 else 0)
  }

  override def part2(strings: Seq[String]): Long = {
    val in = new HyperCubeGrid[Conway](java.util.List.of(java.util.List.of(strings.map(s => s.chars()
      .mapToObj(c => Conway(c == '#'))
      .collect(Collectors.toList[Conway]))
      .asJava)))
    var curr = in
    Range(0, 6)
      .foreach(i => {
        if (debug) {
          println(s"iteration: $i.")
          logHyperCube(curr)
        }
        curr = curr.expandOneStep(Conway(_))
        curr = curr.map((c, p) => c.cycle(curr.getSurroundingTiles(p).asScala))
      })
    curr.sum(c => if (c.active) 1 else 0)
  }

  private def logCube(next: CubeGrid[Day17.Conway]): Unit = {
    for (z <- Range(0, next.depth())) {
      println(s"- z=$z")
      println(LogUtils.tiles(next.getRaw.get(z)))
    }
  }

  private def logHyperCube(next: HyperCubeGrid[Day17.Conway]): Unit = {
    for (w <- Range(0, next.metaDepth()); z <- Range(0, next.depth())) {
      println(s"- w=$w z=$z")
      println(LogUtils.tiles(next.getRaw.get(w).get(z)))
    }
  }

  case class Conway(active: Boolean) extends Loggable {
    def cycle(neighbors: Iterable[Conway]): Conway = {
      val count = neighbors
        .count(t => t.active)
      if (active) Conway(count == 2 || count == 3) else Conway(count == 3)
    }

    override def showTile(): _root_.java.lang.String = if (active) "#" else "."
  }

  object Conway {
    def apply(point: Point3D): Conway = Conway(false)

    def apply(point: TimePoint): Conway = Conway(false)
  }

}
