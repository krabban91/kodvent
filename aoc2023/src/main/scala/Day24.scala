import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day24 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(Hailstorm(_))
    val testArea = if (strings.size > 10) (200000000000000L, 400000000000000L) else (7L, 27L)

    val size = v.zipWithIndex.flatMap { case (a, i) => v.drop(i + 1).filter{b =>
      //println(s"Hailstorm A: $a")
      //println(s"Hailstorm B: $b")
      a.intersection2d(b, testArea)
    } }.size
    size
  }

  override def part2(strings: Seq[String]): Long = {
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
      val inFuture = hit
        .filterNot { case (x, y) =>
          math.signum(x - this.pos._1) != math.signum(this.velocity._1) ||
            math.signum(x - b.pos._1) != math.signum(b.velocity._1) ||
            math.signum(y - this.pos._2) != math.signum(this.velocity._2) ||
            math.signum(y - b.pos._2) != math.signum(b.velocity._2)
        }
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

    private def intersection2(other: Hailstorm): Option[(Double, Double)] = {
      val (x1, y1) = (this.pos._1, this.pos._2)
      val (x2, y2) = (this.pos._1 + this.velocity._1, this.pos._2 + this.velocity._2)
      val (x3, y3) = (other.pos._1, other.pos._2)
      val (x4, y4) = (other.pos._1 + other.velocity._1, other.pos._2 + other.velocity._2)
      val denom = ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1)).toDouble
      if (denom == 0) { None } else {
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
