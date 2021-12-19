import Day19.{ScanResult, inputs}
import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.{Distances, Point3D}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Day19 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val scanners = inputs(strings)
    val known: Map[Point3D, Day19.ScanResult] = triangulate(scanners)

    known.values.flatMap(_.beacons).toSet.size
  }

  override def part2(strings: Seq[String]): Long = -1

  private def triangulate(scanners: Seq[ScanResult]): Map[Point3D, ScanResult] = {
    val known = mutable.HashMap[Point3D, ScanResult]()
    known.put(new Point3D(0, 0, 0), scanners.head)
    val tested = mutable.HashSet[ScanResult]()

    val unknown = mutable.HashSet[ScanResult]()
    unknown.addAll(scanners.tail)
    while (unknown.nonEmpty) {
      println(s"Known:   ${known.values.map(_.id)}")
      println(s"Unknown: ${unknown.map(_.id)}")
      known.values.toSet.diff(tested).foreach(k => {
        unknown.foreach(other => {
          if (unknown.contains(other)) {
            val found = k.findMatch(other, 12)
            if (found.isDefined) {
              unknown.remove(other)
              known.put(found.get._2, found.get._1)
            }
          }
        })
        tested.add(k)
      })
    }
    known.toMap
  }

  case class ScanResult(id: Int, beacons: Seq[Point3D]) {
    def rotations: Seq[((Int, Int, Int), ScanResult)] = {
      val flips = ListBuffer[((Int, Int, Int), (Int, Int, Int))]()
      var nextX = (1, 2, 3)
      var revX = (1,2,3)
      for (i <- (0 until 4)) {
        val t = (nextX, revX)
        flips += t
        val inv = (-nextX._1, nextX._2, nextX._3)
        val revInv = (-revX._1, revX._2, revX._3)
        val tInv = (inv, revInv)
        flips += tInv
        nextX = (nextX._1, -nextX._3, nextX._2)
        revX = (revX._1, revX._3, -revX._2)
      }
      var nextY = (1, 2, 3)
      var revY = (1,2,3)
      for (i <- (0 until 4)) {
        val t = (nextY, revY)
        flips += t
        val inv = (nextY._1, -nextY._2, nextY._3)
        val revInv = (revY._1, -revY._2, revY._3)
        val tInv = (inv, revInv)
        flips += tInv
        nextY = (-nextY._3, nextY._2, nextY._1)
        revY = (revY._3, revY._2, -revY._1)
      }
      var nextZ = (1, 2, 3)
      var revZ = (1, 2, 3)
      for (i <- (0 until 4)) {
        val t = (nextZ, revZ)
        flips += t
        val inv = (nextZ._1, nextZ._2, -nextZ._3)
        val revInv = (revZ._1, revZ._2, -revZ._3)
        val tInv = (inv, revInv)
        flips += tInv
        nextZ = (-nextZ._2, nextZ._1, nextZ._3)
        revZ = (nextZ._2, -nextZ._1, nextZ._3)
      }

      flips.toSeq.distinct.map(t => (t._2, ScanResult(id, beacons
        .map(p => pointFrom(t._1, p)))))

    }

    def pointFrom(is: (Int, Int, Int), p: Point3D): Point3D = {
      new Point3D(getPoint(is._1, p), getPoint(is._2, p), getPoint(is._3, p))
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

    def findMatch(other: ScanResult, count: Int): Option[(ScanResult, Point3D)] = {
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
              (ScanResult(res.id, oAdjusted), diff)
            })
            .map((t._1, _))})
        .find(_ => true)
        .flatMap(t => t._2._1
          .rotations
          .flatMap(res => this.overlaps(res._2, count).map(v =>(res._1, (res._2, v))))
          .find(_ => true)
          .map(tt => {
            val (revMap, (oldRes, oldDiff)) = t
            val (nRev, (res, diff)) = tt
            val oAdjusted = res.beacons.map(p => new Point3D(
              p.getX - diff.getX,
              p.getY - diff.getY,
              p.getZ - diff.getZ))
            (ScanResult(res.id, oAdjusted), pointFrom(revMap, oldDiff.add(pointFrom(nRev, diff))))
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
