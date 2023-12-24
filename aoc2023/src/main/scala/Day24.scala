import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day24 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(Hailstorm(_))
    //val testArea = (7L, 27L)
    val testArea = (200000000000000L, 400000000000000L)
    v.zipWithIndex.flatMap { case (a, i) => v.drop(i + 1).filter(b => a.intersection2d(b, testArea)) }.size
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
      val hit = intersection(b)
      val within = hit.filter { case (x, y) => x >= test._1 && x <= test._2 && y >= test._1 && y <= test._2 }
      val inFuture = within
        .filterNot { case (x, y) =>
          math.signum(x - this.pos._1) != math.signum(this.velocity._1) ||
            math.signum(x - b.pos._1) != math.signum(b.velocity._1) ||
            math.signum(y - this.pos._2) != math.signum(this.velocity._2) ||
            math.signum(y - b.pos._2) != math.signum(b.velocity._2)
        }
      inFuture.isDefined
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
