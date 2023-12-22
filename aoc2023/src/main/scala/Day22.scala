import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples.RichTuples3Longs

import scala.collection.mutable

object Day22 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  case class Brick(from: (Long, Long, Long), to: (Long, Long, Long)) {
    def volume: Long = {
      val (dx, dy, dz) = (from delta to) + (1, 1, 1)
      dx * dy * dz
    }

    def xyOverlaps(other: Brick): Boolean = {
      xOverLap(other) && yOverLap(other)
    }

    def xOverLap(other: Brick): Boolean = {
      overlap(from._1, (other.from._1, other.to._1)) || overlap(to._1, (other.from._1, other.to._1)) ||
        overlap(other.from._1, (from._1, to._1)) || overlap(other.to._1, (from._1, to._1))
    }

    def yOverLap(other: Brick): Boolean = {
      overlap(from._2, (other.from._2, other.to._2)) || overlap(to._2, (other.from._2, other.to._2)) ||
        overlap(other.from._2, (from._2, to._2)) || overlap(other.to._2, (from._2, to._2))
    }
  }

  private def overlap(p: Long, other: (Long, Long)) = {
    p >= math.min(other._1, other._2) && p <= math.max(other._1, other._2)
  }

  object Brick {
    def apply(str: String): Brick = {
      val s = str.split("~")
      val l = s.head.split(",").map(_.toLong)
      val r = s.last.split(",").map(_.toLong)
      Brick((l(0), l(1), l(2)), (r(0), r(1), r(2)))
    }
  }

  override def part1(strings: Seq[String]): Long = {
    val supports: mutable.HashMap[Day22.Brick, Seq[Day22.Brick]] = parse(strings)

    supports.count { case (b, bs) =>
      bs.forall(v => supports.exists(o => o._1 != b && o._2.contains(v)))
    }
  }


  override def part2(strings: Seq[String]): Long = {
    val supports: mutable.HashMap[Day22.Brick, Seq[Day22.Brick]] = parse(strings)
    val topples = supports.map { case (b, bs) =>
      val supportCopy= supports.clone()

      val frontier = mutable.Queue[Brick]()
      frontier.addOne(b)
      val toppled = mutable.HashSet[Brick]()
      while (frontier.nonEmpty) {
        val pop = frontier.dequeue()
        toppled.addOne(pop)
        val holds = supportCopy(pop)
        supportCopy.remove(pop)
        val possible = holds.filterNot(v => supportCopy.exists(o => o._2.contains(v)))
        frontier.addAll(possible)
      }
      toppled.size - 1
    }
    topples.sum
  }

  private def parse(strings: Seq[String]) = {
    val bs = strings.map(Brick(_))
    val closestToGround = bs.sortBy(b => math.min(b.from._3, b.to._3))
    val landed = closestToGround.foldLeft(Seq[Brick]()) { case (land, b) =>
      val relevant = land.filter(_.xyOverlaps(b)).sortBy(b => math.max(b.from._3, b.to._3))
      val y = if (relevant.nonEmpty) {
        // fall to brick
        val top = relevant.last
        math.max(top.from._3, top.to._3) + 1
      } else {
        // fall to ground
        1
      }

      val curY = math.min(b.from._3, b.to._3)
      val dy = math.abs(y - curY)

      val fallen = Brick(b.from - (0, 0, dy), b.to - (0, 0, dy))
      land :+ fallen
    }
    val supports = mutable.HashMap[Brick, Seq[Brick]]()
    val byMyinY = landed.groupBy(b => math.min(b.from._3, b.to._3))
    byMyinY.foreach { case (minY, bs) =>
      bs.map { b =>
        val maxY = math.max(b.from._3, b.to._3)
        val possible = byMyinY.getOrElse(maxY + 1, Seq())
        supports.put(b, possible.filter(_.xyOverlaps(b)))
      }
    }
    supports
  }

}
