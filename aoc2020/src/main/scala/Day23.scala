import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day23 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2


  override def part1(strings: Seq[String]): Long = {
    var cups: Seq[Int] = strings.head.map(c => Integer.parseInt(c.toString))
    var current = cups.head
    val moves = 100
    val maxV = cups.max
    val minV = cups.min
    for (move <- Range(0, moves)) {
      val res = moveCups(cups, current, move, minV, maxV)
      cups = res._1
      current = res._2
    }
    val i = cups.indexOf(1)
    val out = cups.indices.map(v => cups((i + v) % cups.size)).tail
    out.map(_.toString).reduce((l, r) => l + r).toLong
  }

  override def part2(strings: Seq[String]): Long = -1

  def moveCups(cups: Seq[Int], current: Int, move: Int, minV: Int, maxV: Int): (Seq[Int], Int) = {

    val debug = false
    val pickedup = Range(0, 3).map(v => cups((cups.indexOf(current) + 1 + v) % cups.size))

    val next = cups.filterNot(c => pickedup.contains(c))
    var destination = current
    var dIndex = -1
    do {
      destination = (destination - 1)
      if (destination < minV) {
        destination = maxV
      }
      dIndex = next.indexOf(destination)
    } while (dIndex < 0)
    if (debug) {
      println(s"-- move ${move + 1} --")
      println(s"cups: $cups")
      println(s"current: $current")
      println(s"pick up: $pickedup")
      println(s"destination: $destination")

    }
    val res = next.slice(0, dIndex + 1) ++ pickedup ++ next.slice(dIndex + 1, next.size)
    (res,
      res((res.indexOf(current) + 1) % res.size))
  }
}
