import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day17 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.head.stripPrefix("target area: ").split(",").map(_.split("=").last).map(_.split("\\.\\.")).map(_.map(_.toInt))
    val (xs, ys) = ((v.head.head, v.head.last), (v.last.head, v.last.last))
    val initialVelocities: Seq[(Int, Int)] = (-200 to 200).flatMap(x => ((-200 to 200).map(y => (x, y))))

    val initPos = (0, 0)
    val maxVel = initialVelocities.foldLeft(((-1, -1), -1))((t, vel) => {
      val (maxV, maxY) = t
      var pos = initPos
      var xVel = vel._1
      var yVel = vel._2
      var outV = maxV
      var outY = maxY
      var my = -1
      while (!passed(pos, xs, ys)) {
        pos = (pos._1 + xVel, pos._2 + yVel)
        my = if (pos._2 > my) pos._2 else my
        if (within(pos, xs, ys)) {
          if (my > maxY) {
            outV = vel
            outY = my
          }
        }
        xVel = if (xVel == 0) xVel else (xVel + (if (xVel > 0) -1 else 1))
        yVel -= 1
      }
      (outV, outY)
    })
    maxVel._2
  }

  override def part2(strings: Seq[String]): Long = -1

  private def within(pos: (Int, Int), rangeX: (Int, Int), rangeY: (Int, Int)) = {
    (rangeX._1 to rangeX._2).contains(pos._1) && (rangeY._1 to rangeY._2).contains(pos._2)
  }

  private def passed(pos: (Int, Int), rangeX: (Int, Int), rangeY: (Int, Int)) = {
    pos._1 > rangeX._2 || pos._2 < rangeY._2
  }
}
