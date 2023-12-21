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
    // val stepGoal = dEdgeX + w*4
    val cornerLocs: Seq[Long] = cornerLocations(map, w, h, dCorner, stepGoal)
    val midLocs: Seq[Long] = middleLocations(map, sx, sy, w, h, dEdgeX, stepGoal)
    val center: Long = centerLocations(map, start, w, stepGoal)

    println(s"node count: ${map.count(kv => kv._2 == "." || kv._2 == "S")}")



    // 3662092424185928 is too high
    // 1846521620165984 is also too high
    // 615519377087821 is too high
    // 615513342882862 not the right answer
    // 615513291903955 not the right answer
    val sum = midLocs.sum
    val sum1 = cornerLocs.sum
    center + sum + sum1
  }


  def centerLocations(map: Map[(Long, Long), String], start: (Long, Long), w: Int, stepGoal: Int) = {
    val centerStates = gatherState(start, map, w)
    val centerloop = centerStates.toSeq.sortBy(_._1).takeRight(3)
    val centerloopStart = centerloop.head._1
    val compl = if (stepGoal < centerStates.size) {
      centerStates(stepGoal)
    } else {
      centerloop(((stepGoal % centerloopStart) % 2).toInt)._2
    }
    compl
  }

  def cornerLocations(map: Map[(Long, Long), String], w: Int, h: Int, dCorner: Long, stepGoal: Long): Seq[Long] = {
    // -1, -1
    // 1, -1
    // -1, 1
    // 1, 1
    val corners = Seq((0L, 0L), (w - 1L, 0L), (0L, h - 1L), (w - 1L, h - 1L))
    val cornerStates = corners.map(p => (p, gatherState(p, map, w)))
    val cs = (stepGoal - dCorner)
    if (cs < 0) {
      return Seq(0L)
    }
    val cm = cs % w
    val cc = cs / w + 1// plus 1 for the incomplete ones?
    val cregions = (0L until cc).map(_ + 1L).sum
    val cloop = cornerStates.head._2.toSeq.sortBy(_._1).takeRight(3)
    val cloopStart = cloop.head._1
    val incomplete = if (cc > 0) math.min(3, cc) else 0

    val completeI = cc - incomplete
    val complete = (0L until completeI).map(_ + 1).sum
    val cincompleteRegions = cregions - complete
    // the c
    val ccompleteRegions = complete
    val cornerLocs = cornerStates.map { case (p, m) =>
      val loop = cloop
      val loopStart = cloopStart
      val incompl = (0L until incomplete).map { i =>
        val incompleteMul = completeI + 1 + (incomplete - i - 1)
        val left = i * w + cm
        val locs = if (left < 0) {
          0L
        } else if (left < loopStart) {
          m(left)
        } else {
          val mod = (left % loopStart) % 2
          loop(mod.toInt)._2
        }
        locs * incompleteMul
        // todo, fix this
      }
      val compl = loop((cm % 2).toInt)._2 * ccompleteRegions
      incompl.sum + compl
    }
    cornerLocs
  }

  def middleLocations(map: Map[(Long, Long), String], sx: Long, sy: Long, w: Int, h: Int, dEdgeX: Long, stepGoal: Long): Seq[Long] = {
    // 0, -1
    // -1, 0
    // 0, 1
    // 1, 0
    val middles = Seq((sx, 0L), (0L, sy), (sx, h - 1L), (w - 1L, sy))
    val midStates = middles.map(p => (p, gatherState(p, map, w)))
    val mloop = midStates.head._2.toSeq.sortBy(_._1).takeRight(3)
    val mloopStart = mloop.head._1
    val ms = (stepGoal - dEdgeX)
    if (ms < 0) return Seq(0)
    val mm = ms % w
    val mc = ms / w + 1// plus 1 for the incomplete ones?
    val mregions = mc
    val mincompleteRegions = if (mc > 0) math.min(3, mc) else 0
    // the m
    val mcompleteRegions = mregions - mincompleteRegions
    val midLocs = midStates.map { case (p, m) =>
      val loop = mloop
      val loopStart = mloopStart
      val incompl = (0L until mincompleteRegions).map { i =>
        val left = i * w + mm
        val locs = if (left < 0) {
          0L
        } else if (left < loopStart) {
          m(left)
        } else {
          val mod = (left % loopStart) % 2
          loop(mod.toInt)._2
        }
        locs
      }
      val compl = loop((mm % 2).toInt)._2 * mcompleteRegions
      incompl.sum + compl
    }
    midLocs
  }

  def gatherState(start: (Long, Long), map: Map[(Long, Long), String], w: Long): Map[Long, Long] = {

    var it = 0
    val out = mutable.HashMap[Long, Long]()
    var curr = Set(start)
    val allStates = mutable.HashSet[Set[(Long, Long)]]()
    val distances = mutable.HashMap[(Long, Long), Long]()
    while (!allStates.contains(curr)) {
      out.put(it, curr.size)
      allStates.add(curr)
      curr.foreach(p => if (!distances.contains(p)) distances.put(p, it))
      val next = curr.flatMap(neighbors(_, map).map(_._1))
      it += 1
      curr = next
    }
    out.put(it, curr.size)
    val ds = distances.filter(kv => kv._1._1 == 0 || kv._1._2 == 0 || kv._1._1 == w - 1 || kv._1._2 == w - 1).toSeq.sortBy(_._2)
    out.toMap
  }

  def neighbors(p: (Long, Long), map: Map[(Long, Long), String]): Seq[((Long, Long), Long)] = {
    DIRECTIONS.map(_ + p).filter(map.get(_).exists(v => v == "." || v == "S")).map((_, 1))
  }

  def parse(strings: Seq[String]) = {
    val map = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), s"$c") } }.toMap
    val start = map.find(_._2 == "S").map(_._1).get

    (map, start)
  }
}
