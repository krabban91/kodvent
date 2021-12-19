import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.Point3D

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Day19 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val scanners = inputs(strings)
    val known = mutable.HashSet[ScanResult]()
    known.add(scanners.head)
    val unknown = mutable.HashSet[ScanResult]()
    unknown.addAll(scanners.tail)
    while (unknown.nonEmpty) {

      unknown.foreach(other => {
        known.foreach(k => {
          if (unknown.contains(other)) {
            val found = k.findMatch(other, 12)
            if (found.isDefined) {
              unknown.remove(other)
              known.add(found.get)
            }
          }
        })
      })
    }


    known.flatMap(_.beacons).toSet.size
  }

  override def part2(strings: Seq[String]): Long = -1

  case class ScanResult(id: Int, beacons: Seq[Point3D]) {
    def rotations: Seq[((Int, Int, Int), ScanResult)] = {
      val flips = ListBuffer[(Int, Int, Int)]()
      val ff = Seq("x", "-x", "y", "-y", "z", "-z")
      var nextX = (1, 2, 3)
      for (i <- (0 until 4)) {
        flips += nextX
        val inv = (-nextX._1, nextX._2, nextX._3)
        flips += inv
        nextX = (nextX._1, -nextX._3, nextX._2)
      }
      var nextY = (1, 2, 3)
      for (i <- (0 until 4)) {
        flips += nextY
        val inv = (nextY._1, -nextY._2, nextY._3)
        flips += inv
        nextY = (-nextY._3, nextY._2, nextY._1)
      }
      var nextZ = (1, 2, 3)
      for (i <- (0 until 4)) {
        flips += nextZ
        val inv = (nextZ._1, nextZ._2, -nextZ._3)
        flips += inv

        nextZ = (-nextZ._2, nextZ._1, nextZ._3)
      }



      flips.toSeq.distinct.map(t => (t, ScanResult(id, beacons
        .map(p => new Point3D(getPoint(t._1, p), getPoint(t._2, p), getPoint(t._3, p))))))

    }


    def getPoint(i: Int, p: Point3D): Int = i match {
      case 1 => p.getX
      case 2 => p.getY
      case 3 => p.getZ
      case -1 => -p.getX
      case -2 => -p.getY
      case -3 => -p.getZ
    }

    def overlaps(other: ScanResult, count: Int): Option[Point3D] = {
      val thisOrdered = this.beacons.sorted(PointOrder)
      val otherOrdered = other.beacons.sorted(PointOrder)
      val allDiffs = thisOrdered.flatMap(tO => otherOrdered.map(oO => new Point3D(
        oO.getX - tO.getX,
        oO.getY - tO.getY,
        oO.getZ - tO.getZ)))
      allDiffs.find(diff => {
        val oAdjusted = other.beacons.map(p => new Point3D(
          p.getX - diff.getX,
          p.getY - diff.getY,
          p.getZ - diff.getZ)).toSet
        val overlap = this.beacons.toSet.intersect(oAdjusted)
        overlap.size >= count
      })
    }

    def findMatch(other: ScanResult, count: Int): Option[ScanResult] = {
      this.rotations
        .flatMap(t => {
          other.rotations
            .flatMap(res => t._2.overlaps(res._2, count).map((res._2, _)))
            .find(_ => true)
            .map(t => {
              val (res, diff) = t
              val oAdjusted = res.beacons.map(p => new Point3D(
                p.getX - diff.getX,
                p.getY - diff.getY,
                p.getZ - diff.getZ))
              ScanResult(res.id, oAdjusted)
            })
            .map((t._1, _))})
        .find(_ => true)
        .map(_._2)
        .flatMap(t => t
          .rotations
          .flatMap(res => this.overlaps(res._2, count).map((res._2, _)))
          .find(_ => true)
          .map(t => {
            val (res, diff) = t
            val oAdjusted = res.beacons.map(p => new Point3D(
              p.getX - diff.getX,
              p.getY - diff.getY,
              p.getZ - diff.getZ))
            ScanResult(res.id, oAdjusted)
          })
        .find(_ => true))

    }

    def beaconsRelativeToFirst: Seq[Point3D] = {
      val h = beacons.head
      beacons.map(p => new Point3D(p.getX - h.getX, p.getY - h.getY, p.getZ - h.getZ))
    }
  }

  def inputs(string: Seq[String]): Seq[ScanResult] = {
    var curr = string
    val input = ListBuffer[ScanResult]()
    while (curr.nonEmpty) {
      val (scan, next) = ScanResult(curr)
      input += scan
      curr = next
    }
    input.toSeq
  }

  object ScanResult {
    def apply(string: Seq[String]): (ScanResult, Seq[String]) = {
      val useful = string.takeWhile(_.nonEmpty)
      val out = string.drop(useful.size + 1)
      val id = useful.head.stripPrefix("--- scanner ").stripSuffix(" ---").toInt
      val points = useful.tail.map(_.split(",").map(_.toInt)).map(l => new Point3D(l.head, l(1), l.last))
      (ScanResult(id, points), out)
    }
  }

  object PointOrder extends Ordering[Point3D] {
    def compare(a: Point3D, b: Point3D): Int = {
      Option(a.getX compare b.getX)
        .filter(_ != 0)
        .orElse(Option(a.getY compare b.getY))
        .filter(_ != 0)
        .getOrElse(a.getZ compare b.getZ)

    }
  }

}
