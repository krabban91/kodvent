import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day11 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val devices = (strings.map(Device(_)) :+ Device("out", Seq())).map(d => (d.id, d)).toMap
    val memoize = mutable.HashMap[(String, String), Long]()
    countPaths("you", "out", devices, memoize)
  }

  override def part2(strings: Seq[String]): Long = {
    val devices = (strings.map(Device(_)) :+ Device("out", Seq())).map(d => (d.id, d)).toMap
    val memoize = mutable.HashMap[(String, String), Long]()
    val path1 = Seq("svr", "fft", "dac", "out")
    val path2 = Seq("svr", "dac", "fft", "out")
    combinedPath(path1, devices, memoize) + combinedPath(path2, devices, memoize)
  }

  private def countPaths(from: String, to: String, devices: Map[String, Device], memoize: mutable.HashMap[(String, String), Long]): Long = {
    val pair = (from, to)
    if (from == to) {
      1L
    } else if (memoize.contains(pair)) {
      memoize(pair)
    } else {
      val outs = devices(from).out
      val paths = outs.map(signal => countPaths(signal, to, devices, memoize))
      memoize.update(pair, paths.sum)
      memoize(pair)
    }
  }

  private def combinedPath(path1: Seq[String], devices: Map[String, Device], memoize: mutable.HashMap[(String, String), Long]) = {
    path1.sliding(2)
      .map { case from :: to :: Nil => countPaths(from, to, devices, memoize) }.product
  }


  case class Device(id: String, out: Seq[String])

  object Device {
    def apply(str: String): Device = {
      val v = str.split(":")
      val id = v.head.strip()
      val to = v.last.strip().split(" ").toSeq
      Device(id, to)
    }
  }

}
