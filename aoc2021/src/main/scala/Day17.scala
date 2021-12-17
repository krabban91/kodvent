import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day17 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.head.stripPrefix("target area: ").split(",").map(_.split("=").last).map(_.split("\\.\\.")).map(_.map(_.toInt))
    val target = ((v.head.head, v.head.last), (v.last.head, v.last.last))
    initialVelocities(-200, 200).foldLeft((Integer.MIN_VALUE))((maxY, vel) => {
      val (hit, y) = trajectory((0, 0), vel, target)
      if (hit && y > maxY) y else maxY
    })
  }

  override def part2(strings: Seq[String]): Long = {
    val v = strings.head.stripPrefix("target area: ").split(",").map(_.split("=").last).map(_.split("\\.\\.")).map(_.map(_.toInt))
    val target = ((v.head.head, v.head.last), (v.last.head, v.last.last))
    initialVelocities(-200, 200).count(vel => trajectory((0, 0), vel, target)._1)
  }

  def trajectory(initPos: (Int, Int), initVel: (Int, Int), target: ((Int, Int), (Int, Int))): (Boolean, Int) = {
    val (xs, ys) = target
    var pos = initPos
    var xVel = initVel._1
    var yVel = initVel._2
    var hit = false
    var y = -1
    while (!passed(pos, xs, ys)) {
      pos = (pos._1 + xVel, pos._2 + yVel)
      y = if (pos._2 > y) pos._2 else y
      if (within(pos, xs, ys)) {
        hit = true
      }
      xVel = if (xVel == 0) xVel else (xVel + (if (xVel > 0) -1 else 1))
      yVel -= 1
    }
    (hit, y)
  }

  private def initialVelocities(min: Int, max: Int): Seq[(Int, Int)] = {
    (min to max).flatMap(x => (min to max).map(y => (x, y)))
  }

  private def within(pos: (Int, Int), rangeX: (Int, Int), rangeY: (Int, Int)) = {
    (rangeX._1 to rangeX._2).contains(pos._1) && (rangeY._1 to rangeY._2).contains(pos._2)
  }

  private def passed(pos: (Int, Int), rangeX: (Int, Int), rangeY: (Int, Int)) = {
    pos._1 > rangeX._2 || pos._2 < rangeY._1
  }
}
