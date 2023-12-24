import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import implicits.Tuples._

import scala.collection.mutable

object Day24 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(Hailstorm(_))
    val testArea = if (strings.size > 10) (200000000000000L, 400000000000000L) else (7L, 27L)

    val size = v.zipWithIndex.flatMap { case (a, i) => v.drop(i + 1).filter { b =>
      //println(s"Hailstorm A: $a")
      //println(s"Hailstorm B: $b")
      a.intersection2d(b, testArea)
    }
    }.size
    size
  }

  override def part2(strings: Seq[String]): Long = {
    val hs = strings.map(Hailstorm(_))
    val allHail = hs.toSet
    val xs = hs.sortBy(_.pos._1)
    println(xs)
    val time = 0L
    println(s"Testing starting time=$time")
    val frontier = mutable.PriorityQueue[(Set[Hailstorm], (Long, Long, Long), (Long, Long, Long), Long)]()(Ordering.by(v => (-v._1.size, -v._4)))
    allHail.foreach { h =>
      val other = allHail.filterNot(_ == h)
      other.foreach { o =>
        val left = other.filterNot(_ == o)
        left.foreach { o2 =>
          val possible = o.deriveCollisionVelocities(h, o2)
          val without = left.filterNot(_ == o2)
          val next = possible.map { tup =>
            (without, tup._1, tup._2, tup._3)
          }
          if (next.nonEmpty) {
            frontier.addAll(next)
          }

        }
      }
    }
    println("entered frontier land")
    var smallest = allHail.size
    var tested = 0L
    while (frontier.nonEmpty) {
      val pop@(left, start, velocity, time) = frontier.dequeue()
      tested += 1
      if (left.size < smallest) {
        println(s"achieved better: $smallest > ${left.size}")
        smallest = left.size
      }
      if (left.isEmpty) {
        // start found!
        val (x, y, z) = start
        return x + y + z
      }

      // hit or no hit
      val nextLoc = start + velocity * (time, time, time)
      val maybeHailstorm = left.find(h => h.pos + (time, time, time) * h.velocity == nextLoc)
      val hit = maybeHailstorm
      if (hit.isDefined) {
        val h = hit.get
        val without = left.filterNot(_ == h)
        val next = (without, start, velocity, time + 1)
        frontier.addOne(next)
      } else {
        val fakeHail = Hailstorm(start, velocity)
        val nextXY = left.map(g => fakeHail.intersection2(g).flatMap(p => fakeHail.intersectsInFuture(p, g)).map((_, g)))
        if (nextXY.exists(_.isEmpty)) {
          // not relevant path
        } else {
          val next = (left, start, velocity, time + 1)
          frontier.addOne(next)
        }
        // if all left are moving away from trajectory. how do I know?
      }

    }
    -1
  }

  case class Hailstorm(pos: (Long, Long, Long), velocity: (Long, Long, Long)) {

    def line2d: (Long, Long, Long) = {
      // ax + by + c
      val (x, y, _) = pos
      val (dx, dy, _) = velocity
      val a = dy
      val b = -dx
      val c = dx * y - dy * x
      (a, b, c)
    }

    def intersection2d(b: Hailstorm, test: (Long, Long)): Boolean = {
      //val hit = intersection(b)
      val hit = intersection2(b)
      val inFuture = hit.flatMap(intersectsInFuture(_, b))
      val within = inFuture.filter { case (x, y) => x >= test._1 && x <= test._2 && y >= test._1 && y <= test._2 }
      if (hit.isEmpty) {
        //println(s"Hailstones' paths are parallel; they never intersect.")
      } else if (inFuture.isEmpty) {
        //println(s"Hailstones' paths crossed in the past for ???")
      } else if (within.isEmpty) {
        val (x, y) = hit.get
        //println(s"Hailstones' paths will cross outside the test area (at x=$x, y=$y)")
      } else {
        val (x, y) = within.get
        //println(s"Hailstones' paths will cross inside the test area (at x=$x, y=$y)")
      }
      within.isDefined
    }

    def intersection2(other: Hailstorm): Option[(Double, Double)] = {
      val (x1, y1) = (this.pos._1, this.pos._2)
      val (x2, y2) = (this.pos._1 + this.velocity._1, this.pos._2 + this.velocity._2)
      val (x3, y3) = (other.pos._1, other.pos._2)
      val (x4, y4) = (other.pos._1 + other.velocity._1, other.pos._2 + other.velocity._2)
      val denom = ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1)).toDouble
      if (denom == 0) {
        None
      } else {
        val ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom
        val ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom
        val x = x1 + ua * (x2 - x1)
        val y = y1 + ua * (y2 - y1)
        Some((x, y))
      }

    }

    private def intersection(other: Hailstorm): Option[(Double, Double)] = {
      val (a1, b1, c1) = line2d
      val (a2, b2, c2) = other.line2d
      // convert ax + by + c to line functions

      // ax+c=bx+d.
      val a = a1 * b2
      val b = a2 * b1
      val c = b2 * c1
      val d = b1 * c2
      val e = c1 * a2
      val f = c2 * a1
      if (a == b) {
        None
      } else {
        val x = (d - c).toDouble / (a - b).toDouble
        val y = (e - f).toDouble / (a - b).toDouble
        Some(x, y)
      }
    }

    def intersectsInFuture(hit: (Double, Double), b: Hailstorm): Option[(Double, Double)] = {
      val inFuture = Some(hit)
        .filterNot { case (x, y) =>
          math.signum(x - this.pos._1) != math.signum(this.velocity._1) ||
            math.signum(x - b.pos._1) != math.signum(b.velocity._1) ||
            math.signum(y - this.pos._2) != math.signum(this.velocity._2) ||
            math.signum(y - b.pos._2) != math.signum(b.velocity._2)
        }
      inFuture
    }

    def deriveCollisionVelocities(other: Hailstorm, other2: Hailstorm): Seq[((Long, Long, Long), (Long, Long, Long), Long)] = {
      val startPoint = other.pos
      val startVel = other.velocity
      // ((x, y, z), (dx,dy,dx), time)
      // (x0 + t0 * v0x) + (t - t0) * dx = (x1 + t * vx)
      // (x0 + t0 * v0x) + (t - t0) * dx = (x1 + t * vx)
      // dx = ((x1 + t * vx) - (x0 + t0 * v0x)) / (t - t0)

      // x0 + t * dx = x1 + t * vx
      // x0 - x1 = t * vx - t * dx
      // x0 - x1 = t * (vx - dx)
      // vx - (x0 - x1)/t = dx

      // ((dx,dy,dx), timeDelta, initialTime)

      // t = t0 + i
      // (x0 + t0 *v0x)  + t * dx = x1 + t * vx
      // vx - ((x0 + t0 * v0x) - x1) / t = dx


      val list = mutable.ListBuffer[((Long, Long, Long), (Long, Long, Long), Long)]()
      // t0
      val testSize = 500L
      (1L to testSize).foreach { t0 =>
        //println(t0)
        // t
        (t0 + 1L to t0 + testSize).foreach { t =>
          // dx = ((x1 + t * vx) - (x0 + t0 * v0x)) / (t - t0)
          val dx = ((pos._1 + t * velocity._1) - (startPoint._1 + t0 * startVel._1)) / (t - t0)
          val dy = ((pos._2 + t * velocity._2) - (startPoint._2 + t0 * startVel._2)) / (t - t0)
          val dz = ((pos._3 + t * velocity._3) - (startPoint._3 + t0 * startVel._3)) / (t - t0)
          // val dx = velocity._1 - ((startPoint._1 + startVel._1 * t0) - pos._1) / (t)
          // val dy = velocity._2 - ((startPoint._2 + startVel._2 * t0) - pos._2) / (t)
          // val dz = velocity._3 - ((startPoint._3 + startVel._3 * t0) - pos._3) / (t)
          val dVel = (dx, dy, dz)
          val tVec = ((t - t0), (t - t0), (t - t0))
          val t1Vec = ((t), (t), (t))
          val t0Vec = ((t0), (t0), (t0))
          val calc = startPoint + startVel * t0Vec + dVel * tVec
          val target = pos + velocity * t1Vec
          if (calc == target) {
            val startPos = (startPoint + startVel * t0Vec) - dVel * t0Vec
            if (dVel == (-3L, 1L, 2L) && t0 == 1 && t == 3) {
              //println("goalVec")
            }
            (t + 1L to t + testSize).foreach { t2 =>

              val t2Vec = (t2, t2, t2)
              val calc2 = startPos + startVel * t2Vec
              val target2 = other2.pos + other2.velocity * t2Vec

              list.append((startPos, dVel, t2))
              if (calc2 == target2) {
                //println("success")
              }
            }
          }
        }

      }
      list.toSeq
    }
  }


  object Hailstorm {
    def apply(str: String): Hailstorm = {
      val pattern = """(\d), (\d), (\d) @ (\d), (\d), (\d)""".r

      /*
      str match {
        case pattern(x,y,z,dx,dy,dz) => Hailstorm((x.toLong,y.toLong,z.toLong), (dx.toLong,dy.toLong,dz.toLong))
      }

       */

      val spl = str.split("@")
      val ps = spl.head.split(", ").map(_.strip())
      val ds = spl.last.split(", ").map(_.strip())
      Hailstorm((ps(0).toLong, ps(1).toLong, ps(2).toLong), (ds(0).toLong, ds(1).toLong, ds(2).toLong))
    }
  }
}
