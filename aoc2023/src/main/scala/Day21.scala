import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

import scala.collection.mutable

object Day21 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  //printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (map, start) = parse(strings)
    val value = Graph.stepAround[(Long, Long)](Seq(start), p => neighbors(p, map), 64)
    value.size

  }

  def primeFactors(stepGoal: Int): Seq[Int] = {
    var n = 2
    while (stepGoal % n != 0) {
      n += 1
    }
    if (n == stepGoal) {
      Seq(stepGoal)
    } else {
      Seq(n) ++ primeFactors(stepGoal / n)
    }
  }


  override def part2(strings: Seq[String]): Long = {
    val (map, start@(sx, sy)) = parse(strings)
    val (w, h) = (strings.head.length, strings.length)
    val (dEdgeX, dEdgeY) = (sx + 1, sy + 1)
    val dCorner = dEdgeX + dEdgeY

    // after that, it is always w or h. for now: assume w == h

    val stepGoal = 26501365
    // -1, -1
    // 1, -1
    // -1, 1
    // 1, 1
    val cs = (stepGoal - dCorner)
    val cm = cs % w
    val cc = cs / w // plus 0 for the incomplete ones?
    val cregions = (0L to cc).map(_ + 1L).sum
    val cstepsLeft = 260 - cm
    val cIncomplete = cstepsLeft / w + 1
    val cincompleteRegions = (cc -1) + (cc) + (cc + 1)
    // the c
    val ccompleteRegions = cregions - cincompleteRegions
    val corners = Seq((0L, 0L), (w - 1L, 0L), (0L, h - 1L), (w - 1L, h - 1L))
    val cornerStates = corners.map(p => (p, gatherState(p, map, w)))
    val cornerLocs = cornerStates.map{ case (p, m) =>
      val loop = m.toSeq.sortBy(_._1).takeRight(2)
      val loopStart = loop.head._1
      (0 to 2).map{i =>
        val left = i * w + cm
        val locs = if (left < loopStart) {
          m(left)
        } else {
          val mod = (left % loopStart) % 2
          loop(mod.toInt)._2
        }
        val incompl = locs * (cc + 1 - i)
        val compl = loop((cm % 2).toInt)._2 * ccompleteRegions
        incompl + compl
      }
    }
    // 0, -1
    // -1, 0
    // 0, 1
    // 1, 0
    val middles = Seq((sx, 0L), (0L, sy), (sx, h - 1L), (w - 1L, sy))
    val midStates = middles.map(p => (p, gatherState(p, map, w)))

    val ms = (stepGoal - dEdgeX)
    val mm = ms % w
    val mc = ms / w // plus 0 for the incomplete ones?
    val mregions = (0L to mc).map(_ + 1L).sum
    val mstepsLeft = 260 - cm
    val mIncomplete = mstepsLeft / w + 1
    val mincompleteRegions = (cc - 1) + (cc) + (cc + 1)
    // the m
    val mcompleteRegions = mregions - mincompleteRegions
    val midLocs = cornerStates.map { case (p, m) =>
      val loop = m.toSeq.sortBy(_._1).takeRight(2)
      val loopStart = loop.head._1
      (0 to 2).map { i =>
        val left = i * w + cm
        val locs = if (left < loopStart) {
          m(left)
        } else {
          val mod = (left % loopStart) % 2
          loop(mod.toInt)._2
        }
        val incompl = locs * (cc + 1 - i)
        val compl = loop((cm % 2).toInt)._2 * ccompleteRegions
        incompl + compl
      }
    }

    println(s"node count: ${map.count(kv => kv._2 == "." || kv._2 == "S")}")

    val centerStates = gatherState(start, map, w)
    val centerloop = centerStates.toSeq.sortBy(_._1).takeRight(2)
    val centerloopStart = centerloop.head._1
    val compl = centerloop(((stepGoal % centerloopStart) % 2).toInt)._2 * 1
    compl

    compl + midLocs.map(_.sum).sum + cornerLocs.map(_.sum).sum
  }


  def gatherState(start: (Long, Long), map: Map[(Long, Long), String], w: Long): Map[Long, Long] = {

    var it = 0
    val out = mutable.HashMap[Long, Long]()
    var curr = Set(start)
    val allStates = mutable.HashSet[Set[(Long, Long)]]()
    while (!allStates.contains(curr)) {
      out.put(it, curr.size)
      allStates.add(curr)
      val next = curr.flatMap(neighbors(_, map).map(_._1))
      it += 1
      curr = next
    }
    out.put(it, curr.size)
    out.toMap
  }

  def neighbors(p: (Long, Long), map: Map[(Long, Long), String]): Seq[((Long, Long), Long)] = {
    DIRECTIONS.map(_ + p).filter(map.get(_).exists(v => v == "." || v == "S")).map((_, 1))
  }

  private def parse(strings: Seq[String]) = {
    val map = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), s"$c") } }.toMap
    val start = map.find(_._2 == "S").map(_._1).get

    (map, start)
  }
}
